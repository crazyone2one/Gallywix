package cn.master.gallywix.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.FileMetadata;
import cn.master.gallywix.mapper.FileMetadataMapper;
import cn.master.gallywix.service.IFileMetadataService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 *  服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class FileMetadataServiceImpl extends ServiceImpl<FileMetadataMapper, FileMetadata> implements IFileMetadataService {

    @Override
    public void upload(String name, MultipartFile file) throws IOException {
        String result = new BufferedReader(new InputStreamReader(file.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        System.out.printf("upload file: %s, content: \n%s%n", name, result);
    }
}
