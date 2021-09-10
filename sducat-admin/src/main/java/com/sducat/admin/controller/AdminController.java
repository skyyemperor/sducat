package com.sducat.admin.controller;

import com.alibaba.fastjson.JSON;
import com.sducat.common.core.result.Result;
import com.sducat.common.enums.CatStatusEnum;
import com.sducat.common.enums.CommentListTypeEnum;
import com.sducat.common.enums.CommentStatusEnum;
import com.sducat.common.util.ExcelUtil;
import com.sducat.framework.service.TokenService;
import com.sducat.system.data.dto.CommentDto;
import com.sducat.system.data.po.Cat;
import com.sducat.system.service.CatService;
import com.sducat.system.service.CommentService;
import com.sducat.system.service.SysUserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by skyyemperor on 2021-02-01 13:15
 * Description :
 */
@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CatService catService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private TokenService tokenService;

    @PreAuthorize("@pms.hasPerm('admin:excel:export')")
    @GetMapping("/excel/comment")
    public void exportCommentExcel(HttpServletResponse response) {
        HashSet<Long> commentIdSet = new HashSet<>();
        List<CommentDto> comments = new ArrayList<>();
        Result result1 = commentService.getCommentList(tokenService.getUserId(), null, CommentStatusEnum.COMMUNITY.getKey(),
                1, 10000, CommentListTypeEnum.DATE);
        Result result2 = commentService.getCommentList(tokenService.getUserId(), null, CommentStatusEnum.SPECTRUM.getKey(),
                1, 10000, CommentListTypeEnum.DATE);
        List<CommentDto> comments1 = (List<CommentDto>) ((Map) result1.getData()).get("comments");
        List<CommentDto> comments2 = (List<CommentDto>) ((Map) result2.getData()).get("comments");
        for (CommentDto c : comments1) {
            if (!commentIdSet.contains(c.getCommentId())) {
                commentIdSet.add(c.getCommentId());
                comments.add(c);
            }
        }
        for (CommentDto c : comments2) {
            if (!commentIdSet.contains(c.getCommentId())) {
                commentIdSet.add(c.getCommentId());
                comments.add(c);
            }
        }
        comments.sort(Comparator.comparing(CommentDto::getDate));

        String[] headers = new String[]{"评论ID", "用户名", "来自猫咪详情页", "内容", "图片", "发表时间", "点赞数"};
        List<List<String>> data = new ArrayList<>();
        for (CommentDto c : comments) {
            List<String> col = new ArrayList<>();
            col.add(c.getCommentId().toString());
            col.add(c.getPoster().getNickName());
            col.add(c.getCat() == null ? "" : c.getCat().getCatName());
            col.add(c.getContent());
            col.add(JSON.toJSONString(c.getPic()));
            col.add(c.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            col.add(c.getLike().toString());
            data.add(col);
        }

        try {
            exportToExcel(headers, data,
                    URLEncoder.encode("sducat-comments", "UTF-8") + ".xlsx",
                    response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出到excel
     */
    private void exportToExcel(String[] headers, List<List<String>> list, String filename,
                               HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; ++i) {
            Cell cell = header.createCell(i, CellType.STRING);
            cell.setCellValue(headers[i]);
        }
        int rowNo = 1;
        Row row;
        Cell cell;
        for (List<String> item : list) {
            row = sheet.createRow(rowNo++);
            for (int i = 0; i < item.size(); ++i) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(item.get(i));
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\"%s\"", filename));
        workbook.write(response.getOutputStream());
    }


}
