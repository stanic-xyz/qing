package chenyunlong.zhangli.dao;

import chenyunlong.zhangli.entities.UploadFile;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadFile, Long> {

}
