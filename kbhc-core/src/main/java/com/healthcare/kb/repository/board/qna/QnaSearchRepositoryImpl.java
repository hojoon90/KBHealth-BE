package com.healthcare.kb.repository.board.qna;

import com.healthcare.kb.domain.QnaBoard;
import com.healthcare.kb.dto.QnaBoardDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.healthcare.kb.domain.QQnaBoard.qnaBoard;

@Repository
public class QnaSearchRepositoryImpl extends QuerydslRepositorySupport
        implements QnaSearchRepository {

    private final JPAQueryFactory queryFactory;

    public QnaSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        super(QnaBoard.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<QnaBoard> getPagination(QnaBoardDto.SearchCreteria criteria,
                                        Pageable pageable) {

        final JPQLQuery<QnaBoard> query = queryFactory.selectFrom(qnaBoard)
                .where(
                        titleContains(criteria.getTitle())
                )
                .orderBy(qnaBoard.qnaNo.desc());

        final long total_count = query.fetch().size();
        final Querydsl querydsl = Optional.ofNullable(getQuerydsl())
                .orElseThrow(() -> new IllegalStateException("Querydsl instance is not initialized."));

        final List<QnaBoard> qnaBoardList = querydsl.applyPagination(pageable, query).fetch();

        return new PageImpl<>(qnaBoardList, pageable, total_count);
    }

    private BooleanExpression titleContains(final String keyword) {
        return StringUtils.isNotBlank(keyword) ? qnaBoard.title.contains(keyword)
                : null;
    }
}
