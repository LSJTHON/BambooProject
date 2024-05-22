package com.bamboo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReReply is a Querydsl query type for ReReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReReply extends EntityPathBase<ReReply> {

    private static final long serialVersionUID = -1240241447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReReply reReply = new QReReply("reReply");

    public final QBoard board;

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QMember member;

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public final QReply reply;

    public QReReply(String variable) {
        this(ReReply.class, forVariable(variable), INITS);
    }

    public QReReply(Path<? extends ReReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReReply(PathMetadata metadata, PathInits inits) {
        this(ReReply.class, metadata, inits);
    }

    public QReReply(Class<? extends ReReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.reply = inits.isInitialized("reply") ? new QReply(forProperty("reply"), inits.get("reply")) : null;
    }

}

