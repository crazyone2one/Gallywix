package cn.master.gallywix.controller;

import cn.master.gallywix.entity.FileMetadata;
import cn.master.gallywix.service.IFileMetadataService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 *  控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileMetadataController {

    private final IFileMetadataService iFileMetadataService;

    /**
     * 添加。
     *
     * @param fileMetadata 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody FileMetadata fileMetadata) {
        return iFileMetadataService.save(fileMetadata);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iFileMetadataService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param fileMetadata 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody FileMetadata fileMetadata) {
        return iFileMetadataService.updateById(fileMetadata);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<FileMetadata> list() {
        return iFileMetadataService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public FileMetadata getInfo(@PathVariable Serializable id) {
        return iFileMetadataService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<FileMetadata> page(Page<FileMetadata> page) {
        return iFileMetadataService.page(page);
    }

    @PostMapping("/file/upload")
    public void upload(MultipartFile file) throws IOException {
        iFileMetadataService.upload(file.getOriginalFilename(), file);
    }
}
