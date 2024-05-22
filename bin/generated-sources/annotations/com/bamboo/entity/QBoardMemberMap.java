package com.bamboo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardMemberMap is a Querydsl query type for BoardMemberMap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardMemberMap extends EntityPathBase<BoardMemberMap> {

    private static final long serialVersionUID = 217839482L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardMemberMap boardMemberMap = new QBoardMemberMap("boardMemberMap");

    public final QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QBoardMemberMap(String variable) {
        this(BoardMemberMap.class, forVariable(variable), INITS);
    }

    public QBoardMemberMap(Path<? extends BoardMemberMap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardMemberMap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardMemberMap(PathMetadata metadata, PathInits inits) {
        this(BoardMemberMap.class, metadata, inits);
    }

    public QBoardMemberMap(Class<? extends BoardMemberMap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

