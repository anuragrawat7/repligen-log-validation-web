package web.repligenvalidation.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import web.repligenvalidation.Entities.ExceptionEntity;
import web.repligenvalidation.Service.ValidationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<String> addExceptionIntoDB(ExceptionEntity exceptionEntity, @RequestParam(value = "folder",required = true)
            MultipartFile[] multipartFiles) throws IOException {
        //Read the file one by one
        for (MultipartFile multiPartFile : multipartFiles) {
            if(multiPartFile.isEmpty()){
                return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
            }
            else {
                InputStream inputStream = multiPartFile.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    if (line.contains("Exception")) {
                        //read only those file have which have Exception keyword.
                        exceptionEntity = readLogFile(multiPartFile);
                        break;
                    }
                }
                validationService.addExceptionLog(exceptionEntity);
            }
        }
        return new ResponseEntity<>("<h1>Data Insert Successfully</h1>", HttpStatus.CREATED);
    }


     ExceptionEntity readLogFile(MultipartFile multiPartFile) throws IOException {

        ExceptionEntity exceptionEntity = new ExceptionEntity();
        InputStream inputStream = multiPartFile.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //All variable define
        int lineNumber = 0;
        String exceptionName = null;
        String fileName;
        String anchor = null;
        StringBuilder expectionLines = new StringBuilder();
        String version = null;
        String uuid = null;
        String line,nextLine;
        Map<Integer, String> map = new HashMap<>();
        int currentLine = 0, previousPosition;

        //FileName of particular file
        fileName = multiPartFile.getOriginalFilename();
        System.out.println(fileName);
        while((line = bufferedReader.readLine())!=null) {
            currentLine++;
            map.put(currentLine, line);
            if (line.contains("kfcomm_version")) {
                version = line.substring(line.indexOf(":") + 2);
            }
            if (line.contains("uuid")) {
                uuid = line.substring(line.indexOf(":") + 3);
            }
            if (line.contains("Exception")) {

                //Store Exception Name
                exceptionName = line;
                System.out.println(exceptionName);

                //Store Line number
                lineNumber = currentLine;
                System.out.println(lineNumber);

                anchor = getExceptionAnchor(line);

                // To read previous 15 lines from the current exception
                previousPosition = currentLine - 15;
                for (int i = 0; i <= 15; i++) {
                    if (map.get(previousPosition) != null) {
                        expectionLines.append(map.get(previousPosition));
                    }
                    previousPosition++;
                }

                // To read next 15 lines from the current exception
                for (int i = currentLine; i < (currentLine + 15); i++) {
                    nextLine = bufferedReader.readLine();
                    if (nextLine != null) {
                        expectionLines.append(nextLine);
                    } else {
                        break;
                    }
                }
                break;
            }
        }

        exceptionEntity.setLineNumber(lineNumber);
        exceptionEntity.setExceptionName(exceptionName);
        exceptionEntity.setFILENAME(fileName);
        exceptionEntity.setAnchor(anchor);
        exceptionEntity.setExpectionLines(expectionLines.toString());
        exceptionEntity.setVersion(version);
        exceptionEntity.setUUID(uuid);

        return exceptionEntity;
    }

    public String getExceptionAnchor(String line){
        int beginIndex= 0, endIndex=0;
        beginIndex = line.lastIndexOf("Exception");
        endIndex = beginIndex + 9;
        if (line.length() == endIndex || line.charAt(endIndex) == '.' || line.charAt(endIndex) == ':'
                || line.charAt(endIndex) == ' ') {
            endIndex = line.length();
        }

        for (int i = beginIndex; i > 0; i--) {
            if (line.charAt(i) == '.' || line.charAt(i) == ' ') {
                beginIndex = i;
                break;
            }
        }
        return line.substring(beginIndex + 1, endIndex);
    }
}
