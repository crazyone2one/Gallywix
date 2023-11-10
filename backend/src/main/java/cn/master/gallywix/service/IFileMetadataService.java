package cn.master.gallywix.service;

import cn.master.gallywix.entity.FileMetadata;
import com.mybatisflex.core.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface IFileMetadataService extends IService<FileMetadata> {
    void upload(String name, MultipartFile file) throws IOException;

}
