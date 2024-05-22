package com.bamboo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileConfig is a Querydsl query type for FileConfig
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileConfig extends EntityPathBase<FileConfig> {

    private static final long serialVersionUID = 2071639612L;

    public static final QFileConfig fileConfig = new QFileConfig("fileConfig");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> maxFileCount = createNumber("maxFileCount", Long.class);

    public final NumberPath<Long> maxFileSize = createNumber("maxFileSize", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public QFileConfig(String variable) {
        super(FileConfig.class, forVariable(variable));
    }

    public QFileConfig(Path<? extends FileConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileConfig(PathMetadata metadata) {
        super(FileConfig.class, metadata);
    }

}

