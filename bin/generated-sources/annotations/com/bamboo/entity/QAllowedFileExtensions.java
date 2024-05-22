package com.bamboo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAllowedFileExtensions is a Querydsl query type for AllowedFileExtensions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAllowedFileExtensions extends EntityPathBase<AllowedFileExtensions> {

    private static final long serialVersionUID = 2000942426L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAllowedFileExtensions allowedFileExtensions = new QAllowedFileExtensions("allowedFileExtensions");

    public final StringPath extension = createString("extension");

    public final QFileConfig fileConfig;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAllowedFileExtensions(String variable) {
        this(AllowedFileExtensions.class, forVariable(variable), INITS);
    }

    public QAllowedFileExtensions(Path<? extends AllowedFileExtensions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAllowedFileExtensions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAllowedFileExtensions(PathMetadata metadata, PathInits inits) {
        this(AllowedFileExtensions.class, metadata, inits);
    }

    public QAllowedFileExtensions(Class<? extends AllowedFileExtensions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fileConfig = inits.isInitialized("fileConfig") ? new QFileConfig(forProperty("fileConfig")) : null;
    }

}

