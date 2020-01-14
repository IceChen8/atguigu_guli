package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Chen
 * @since 2019-12-25
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public List<String> importExcl(MultipartFile file) {

        List<String> messageList = new ArrayList<>();

        try {
            //1、获取文件流
            InputStream fileInputStream = file.getInputStream();
            //2、根据流创建一个Workbook对象
            Workbook workbook = new HSSFWorkbook(fileInputStream);
            //3、获取表格
            Sheet sheet = workbook.getSheetAt(0);
            //4、获取行：最后一行
            int lastRowNum = sheet.getLastRowNum();
            //5、判断最后一行是否大于0
            if (lastRowNum <= 0){
                //6、如果小于等于0，不再执行了，直接返回提示
                messageList.add("此文件没有课程分类数据");
                return messageList;
            }
            //7、如果大于0,遍历行
            for(int rowNum = 1; rowNum <= lastRowNum; rowNum++){

                //8、获取每一行
                Row row = sheet.getRow(rowNum);
                //9、判断每一行是否存在
                if(row != null){//10、如果存在才往下面执行；
                    //11、获取列
                    Cell cell = row.getCell(0);
                    //12、判断列是否存在
                    if(cell == null){
                        //13、如果不存在提示某一行的某一列为空
                        messageList.add("第"+(rowNum+1)+"行，第1列为空");
                        continue;
                    }
                    //14、如果存在，获取数据
                    String stringCellValue = cell.getStringCellValue();
                    //15、判断数据是否为空
                    if(StringUtils.isEmpty(stringCellValue)){
                        //16、如果数据为空：返回数据提示，不往下执行了；
                        messageList.add("第"+(rowNum+1)+"行，第1列为空");
                        continue;
                    }
                    //17、判断这是一级分类数据和parentId = 0;是否存在
                    Subject subject = this.isSubjectByTitleAndParentId(stringCellValue, "0");
                    String subjectOneId = null;
                    if(subject == null){
                        //19、如果不存在，把此一级分类数据添加到数据库中国一级分类，返回Id
                        subject = new Subject();
                        subject.setParentId("0");
                        subject.setSort(rowNum);
                        subject.setTitle(stringCellValue);
                        baseMapper.insert(subject);
                        subjectOneId = subject.getId();
                    } else{
                        //18、如果存在，获取Id；
                        subjectOneId = subject.getId();
                    }
                    //20、还是这一行，获取第二列
                    Cell cell1 = row.getCell(1);
                    //21、判断第二列是否存在
                    if(cell1 == null){
                        //22、如果不存在直接返回，提示为空
                        messageList.add("第"+(rowNum+1)+"行，第2列为空");
                        continue;
                    }
                    //23、如果存在获取数据
                    String value = cell1.getStringCellValue();
                    //24、判断数据是否存在
                    if(StringUtils.isEmpty(value)){
                        //25、如果不存在，直接返回提示
                        messageList.add("第"+(rowNum+1)+"行，第2列为空");
                        continue;
                    }
                    //26、如果存在，判断此二级分类的名称和刚才添加的一级分类的Id为parentId，查询是否存在
                    Subject subjectTwo = this.isSubjectByTitleAndParentId(value, subjectOneId);
                    //27、如果不存在添加二级分类
                    if(subjectTwo == null){
                        subjectTwo = new Subject();
                        subjectTwo.setParentId(subjectOneId);
                        subjectTwo.setSort(rowNum);
                        subjectTwo.setTitle(value);
                        baseMapper.insert(subjectTwo);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return messageList;
    }

    @Override
    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> mapList = new ArrayList<>();

        //1、先获取一级分类集合
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.orderByAsc("sort");
        List<Subject> subjectOneList = baseMapper.selectList(wrapper);
        //2、遍历一级分类集合
        for(Subject subject : subjectOneList){
            //3、获取每一个一级下面的二级分类（集合）
            Map<String, Object> mapOne = new HashMap<>();
            mapOne.put("id", subject.getId());
            mapOne.put("title", subject.getTitle());
            QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("sort");
            queryWrapper.eq("parent_id", subject.getId());
            List<Subject> selectTwoList = baseMapper.selectList(queryWrapper);
            //4、直接把集合放在Map
            mapOne.put("children", selectTwoList);
            //5、把每一个一级分类的Map放在总集合中
            mapList.add(mapOne);
        }

        return mapList;
    }

    @Override
    public void removeSubjectById(String id) {
        Subject subjectById = baseMapper.selectById(id);
        if (subjectById.getParentId().equals("0")){
            Map<String, Object> map = new HashMap<>();
            map.put("parent_id",id);
            baseMapper.deleteByMap(map);
        }
        baseMapper.deleteById(id);
    }

    @Override
    public boolean saveLevel(Subject subject) {

        return false;
    }

    @Override
    public List<Subject> getSubjectListByParentId(String parentId) {

        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",parentId);
        List<Subject> subjectList = baseMapper.selectList(wrapper);

        return subjectList;
    }

    /**
     * 根据课程分类的名称和parentid查询此分类是否存在
     * @param stringCellValue
     * @param parentId
     * @return
     */
    private Subject isSubjectByTitleAndParentId(String stringCellValue, String parentId) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",parentId);
        wrapper.eq("title",stringCellValue);
        Subject subject = baseMapper.selectOne(wrapper);
        return subject;
    }
}
