package Spring.MindStone.service;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.GeneralException;
import Spring.MindStone.repository.DiaryRepository.DiaryImageRepository;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final DiaryImageRepository diaryImageRepository;

    public String uploadFile(MultipartFile file) {
        if (file == null) {
            return null;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String fileName = UUID.randomUUID().toString();
        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    public void deleteFile(String filePath) {
        if (filePath.isBlank() || filePath == null) {
            return;
        }
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (Exception e) {
            System.out.println("FileService: IOException at stream() -> " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("S3 삭제에 실패하였습니다.");
        }
    }

}
