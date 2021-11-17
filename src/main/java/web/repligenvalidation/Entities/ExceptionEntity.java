package web.repligenvalidation.Entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="ExceptionLog")
public class ExceptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int LogId;

    @Column(name = "TIMESTAMP")
    private Date TIMESTAMP  = new Date();

    @Column(name="LineNumber")
    private Integer LineNumber;

    @Column(name="ExpectionName")
    private String ExceptionName;

    @Column(name="FileName")
    private String FILENAME;

    @Column(name="Anchor")
    private String Anchor;

    @Column(name="ExpectionLines", columnDefinition = "text")
    private String ExpectionLines;

    @Column(name="Version")
    private String Version;

    @Column(name="UUID")
    private String UUID;
}

