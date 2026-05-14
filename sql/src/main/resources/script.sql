
-- 회원 테이블
drop table tblUser;

create table tblUser (
    id varchar2(50) primary key,
    pw varchar2(50) not null,
    name varchar2(50) not null
);

-- 회원 정보 테이블(1:1)
drop table tblUserInfo;

create table tblUserInfo (
    id varchar2(50) primary key,
    age number(3) not null,
    address varchar2(500) not null,
    gender char(1) null,
    constraint userinfo_fk foreign key(id) references tblUser(id)
);

-- 게시판 테이블
drop table tblBoard;
drop sequence seqBoard;

create table tblBoard (
    seq number primary key,
    subject varchar2(1000) not null,
    content varchar2(4000) not null,
    regdate date default sysdate not null,
    id varchar2(50) not null references tblUser(id)
);
create sequence seqBoard start with 11;

-- 댓글 테이블(1:N)
drop table tblComment;
drop sequence seqComment;

create table tblComment (
    seq number primary key,
    subject varchar2(1000) not null,
    regdate date default sysdate not null,
    id varchar2(50) not null references tblUser(id),
    bseq number not null references tblBoard(seq)
);
create sequence seqComment start with 16;

-- 태그 테이블
drop table tblTag;
drop sequence seqTag;

create table tblTag (
    seq number primary key,
    tag varchar2(100) not null
);
create sequence seqTag start with 6;

-- 태깅 테이블
drop table tblTagging;
drop sequence seqTagging;

create table tblTagging (
    seq number primary key,
    tseq number not null references tblTag(seq),
    bseq number not null references tblBoard(seq)
);
create sequence seqTagging start with 11;


----------------------------------------------------------------------------


-- tblUser
insert into tblUser (id, pw, name) values ('hong', '1111', '홍길동');
insert into tblUser (id, pw, name) values ('dog', '1111', '강아지');
insert into tblUser (id, pw, name) values ('cat', '1111', '고양이');
insert into tblUser (id, pw, name) values ('tiger', '1111', '호랑이');
insert into tblUser (id, pw, name) values ('lion', '1111', '사자');
commit;

-- tblUserInfo
insert into tblUserInfo (id, age, address, gender) values ('hong', '20', '서울시 강남구 대치동', 'm');
insert into tblUserInfo (id, age, address, gender) values ('dog', '3', '서울시 강남구 역삼동', 'm');
insert into tblUserInfo (id, age, address, gender) values ('cat', '2', '서울시 강남구 압구정동', 'f');

-- tblBoard
insert into tblBoard (seq, subject, content, regdate, id)
    values (1, 'JPA 수업 중입니다.', '내용', sysdate - 3, 'hong');
insert into tblBoard (seq, subject, content, regdate, id)
    values (2, '멍멍🦴 멍멍멍!!', '내용', sysdate - 2.8, 'dog');
insert into tblBoard (seq, subject, content, regdate, id)
    values (3, '냐옹 냐옹😿 밥줘라~🍚', '내용', sysdate - 2.5, 'cat');
insert into tblBoard (seq, subject, content, regdate, id)
    values (4, '스프링 부트는 어렵다;;', '내용', sysdate - 2.2, 'hong');
insert into tblBoard (seq, subject, content, regdate, id)
    values (5, '배고프다. 점심이 언제냐ㅠ', '내용', sysdate - 2.121, 'hong');
insert into tblBoard (seq, subject, content, regdate, id)
    values (6, '사료를 대령해라 집사야!!', '내용', sysdate - 2.0021, 'cat');
insert into tblBoard (seq, subject, content, regdate, id)
    values (7, '버그 잡기가 힘들어요. 디버깅 힘들어', '내용', sysdate - 1.92, 'hong');
insert into tblBoard (seq, subject, content, regdate, id)
    values (8, '개껌 물고 산책가능 중~', '내용', sysdate - 1.1, 'dog');
insert into tblBoard (seq, subject, content, regdate, id)
    values (9, '산책 갔다와서 목욕했음 🐶🛁', '내용', sysdate - 0.7, 'dog');
insert into tblBoard (seq, subject, content, regdate, id)
    values (10, '프로젝트 진행중입니다.', '내용', sysdate, 'hong');

-- tblComment
insert into tblComment (seq, subject, regdate, id, bseq) values (1, '사료 대령했습니다.', sysdate - 2.4, 'hong', 3);
insert into tblComment (seq, subject, regdate, id, bseq) values (2, '나도 밥줘라!!🍚', sysdate - 2.3, 'dog', 3);
insert into tblComment (seq, subject, regdate, id, bseq) values (3, '스프링 레거시보단 쉽다.', sysdate - 2.1, 'hong', 4);
insert into tblComment (seq, subject, regdate, id, bseq) values (4, '그래도 어렵다.', sysdate - 1.9, 'hong', 4);
insert into tblComment (seq, subject, regdate, id, bseq) values (5, '완전 정복!! 스프링 부트!!🐝', sysdate - 1.5, 'hong', 4);
insert into tblComment (seq, subject, regdate, id, bseq) values (6, '단위 테스트를 열심히 하자', sysdate - 1.7, 'hong', 7);
insert into tblComment (seq, subject, regdate, id, bseq) values (7, '멍멍!! 디버깅 멍멍!!', sysdate - 1.1, 'dog', 7);
insert into tblComment (seq, subject, regdate, id, bseq) values (8, '나도 데려가라~', sysdate - 1, 'cat', 8);
insert into tblComment (seq, subject, regdate, id, bseq) values (9, '집사도 데려가라~', sysdate - 0.9, 'hong', 8);
insert into tblComment (seq, subject, regdate, id, bseq) values (10, '일찍 들어가보겠습니다.', sysdate - 0.8, 'cat', 8);
insert into tblComment (seq, subject, regdate, id, bseq) values (11, '산책 후 취짐 중', sysdate - 0.5, 'cat', 8);
insert into tblComment (seq, subject, regdate, id, bseq) values (12, '댕댕이 목욕 준비 끝났음.', sysdate - 0.2, 'hong', 8);
insert into tblComment (seq, subject, regdate, id, bseq) values (13, '만족스러운 목욕이었음.', sysdate, 'hong', 8);
insert into tblComment (seq, subject, regdate, id, bseq) values (14, '구현 시작함. 리포지토리 생성 중', sysdate + 0.0001 , 'hong', 9);
insert into tblComment (seq, subject, regdate, id, bseq) values (15, '머지 충돌남. 담당자 불러~⌨️', sysdate + 0.0002 , 'hong', 9);

-- tblTag
insert into tblTag (seq, tag) values (1, '스프링');
insert into tblTag (seq, tag) values (2, '집사');
insert into tblTag (seq, tag) values (3, '휴식');
insert into tblTag (seq, tag) values (4, '댕댕이');
insert into tblTag (seq, tag) values (5, '약속');

-- tblTagging
insert into tblTagging (seq, tseq, bseq) values (1, 1, 1);
insert into tblTagging (seq, tseq, bseq) values (2, 2, 3);
insert into tblTagging (seq, tseq, bseq) values (3, 1, 4);
insert into tblTagging (seq, tseq, bseq) values (4, 1, 7);
insert into tblTagging (seq, tseq, bseq) values (5, 2, 6);
insert into tblTagging (seq, tseq, bseq) values (6, 2, 8);
insert into tblTagging (seq, tseq, bseq) values (7, 1, 10);
insert into tblTagging (seq, tseq, bseq) values (8, 3, 10);
insert into tblTagging (seq, tseq, bseq) values (9, 4, 8);
insert into tblTagging (seq, tseq, bseq) values (10, 4, 9);

commit;

