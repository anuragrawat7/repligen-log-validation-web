package web.repligenvalidation.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import web.repligenvalidation.Entities.ExceptionEntity;
import web.repligenvalidation.Service.ValidationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Controller
@RequestMapping("/api/validate")
public class ValidationController {

    private ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        super();
        this.validationService = validationService;
    }

    @GetMapping
    public String getData() {
        return "KFComm2DebuggerApplication";
    }

    //This POST method is used to add Exception Log data into Database.
    @PostMapping(value = "/debug", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addExceptionIntoDB(ExceptionEntity exceptionEntity, @RequestPart(value = "folder",required = true)
            MultipartFile[] multipartFiles) throws IOException {

        //Read the file one by one
        for(MultipartFile multiPartFile:multipartFiles){
            InputStream inputStream = multiPartFile.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine())!=null){

                if (line.contains("Exception")) {
                    //read only those file have which have Exception keyword.
                    exceptionEntity = ValidationService.readLogFile(multiPartFile);
                    break;
                }
            }
            validationService.addExceptionLog(exceptionEntity);
    }
        return new ResponseEntity<String>("<h1>Data Insert Successfully</h1>",HttpStatus.CREATED);
    }



}






     /*public void readLogFile(MultipartFile multiPartFile) throws IOException {

        InputStream inputStream = multiPartFile.getInputStream();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String ch;
        System.out.println("************************************");
        System.out.println(multiPartFile.getOriginalFilename());
        while((ch=bufferedReader.readLine())!=null){
            System.out.println(ch);
        }
        System.out.println("************************************");
    }*/


    /*@PostMapping(value = "/debug")
    public void addException(@ModelAttribute("folderPath") FolderPathEntity folderPathEntity) throws IOException {
        //System.out.println("File from client is -" + folderPathEntity.getFileName().getParentFile().getAbsolutePath());
        File file = new File(folderPathEntity.getFileName());
        System.out.println(file);
        BufferedReader br
                = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st;
        // Consition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null)

            // Print the string
            System.out.println("*******************");
            System.out.println("File data -" + st);
    }
//        return file;
}
*/
/*
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            System.out.println("********");
            System.out.println("File data length :"+data.length);
            System.out.println("File data  :"+data);
            System.out.println("Data is***************** \n"+new String(data));

            // write
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\Downloads\\"+multipartFile.getOriginalFilename());
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
             */