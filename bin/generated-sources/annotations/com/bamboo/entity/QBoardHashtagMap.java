package com.bamboo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardHashtagMap is a Querydsl query type for BoardHashtagMap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardHashtagMap extends EntityPathBase<BoardHashtagMap> {

    private static final long serialVersionUID = 1097014744L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardHashtagMap boardHashtagMap = new QBoardHashtagMap("boardHashtagMap");

    public final QBoard board;

    public final QHashtag hashtag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBoardHashtagMap(String variable) {
        this(BoardHashtagMap.class, forVariable(variable), INITS);
    }

    public QBoardHashtagMap(Path<? extends BoardHashtagMap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardHashtagMap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardHashtagMap(PathMetadata metadata, PathInits inits) {
        this(BoardHashtagMap.class, metadata, inits);
    }

    public QBoardHashtagMap(Class<? extends BoardHashtagMap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.hashtag = inits.isInitialized("hashtag") ? new QHashtag(forProperty("hashtag")) : null;
    }

}

