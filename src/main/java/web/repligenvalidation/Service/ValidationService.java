package web.repligenvalidation.Service;

import org.springframework.web.multipart.MultipartFile;
import web.repligenvalidation.Entities.ExceptionEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public interface ValidationService {

    static ExceptionEntity readLogFile(MultipartFile multiPartFile) throws IOException {

        ExceptionEntity exceptionEntity = new ExceptionEntity();
        InputStream inputStream = multiPartFile.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
        int beginIndex= 0, endIndex=0;
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
                exceptionName = line;
                System.out.println(exceptionName);
                lineNumber = currentLine;
                System.out.println(lineNumber);

                //For Anchor(Unique Exception)
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
                anchor = line.substring(beginIndex + 1, endIndex);

                // To read previous 25 lines from the current exception
                previousPosition = currentLine - 5;
                for (int i = 0; i <= 5; i++) {
                    if (map.get(previousPosition) != null) {
                        expectionLines.append(map.get(previousPosition));
                    }
                    previousPosition++;
                }

                // To read next 50 lines from the current exception
                for (int i = currentLine; i < (currentLine + 10); i++) {
                    nextLine = bufferedReader.readLine();
                    if (nextLine != null) {
                        expectionLines.append(nextLine);
                    } else {
                        break;
                    }
                }
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

    void addExceptionLog(ExceptionEntity exceptionEntity) throws IOException;

}

