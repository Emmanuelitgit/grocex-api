package com.grocex_api.fileManager;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "productFile_tb")
public class ProductFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private UUID id;
    @Column
    public String fileName;
    @Column
    public String fileId;
    @Column
    public String fileType;
}
