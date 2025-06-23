-- auto-generated definition
create table tb_drools
(
    id         bigint unsigned auto_increment comment '룰 고유 ID'
        primary key,
    rule_group varchar(255)                         not null comment '룰 그룹 명',
    rule_nm    varchar(255)                         not null comment '룰 이름',
    rule_text  longtext                             not null comment '룰 정보',
    enable     tinyint(1) default 0                 not null comment '활성화 여부',
    created_by varchar(255)                         not null comment '룰 등록자',
    created_at timestamp  default CURRENT_TIMESTAMP not null comment '룰 등록일 '
);

-- auto-generated definition
create table tb_drools_audit_history
(
    id              bigint unsigned auto_increment comment '룰 실행 이력 고유 ID'
        primary key,
    rule_name       varchar(255) not null comment '룰 명',
    rule_audit_ip   varchar(255) not null comment '룰 실행 한 IP',
    rule_api_method varchar(255) not null comment '룰 실행 API 메소드',
    rule_api        varchar(255) not null comment '룰 실행 URL',
    rule_fact       text         null comment '룰 입력 FACT',
    rule_result     text         null comment '룰 결과',
    status          varchar(255) not null comment '룰 실행 상태(SUCCESS, FAILED)',
    message         text         null comment '룰 실행 메세지',
    created_by      varchar(255) not null comment '등록 자',
    created_at      timestamp    not null comment '등록 일'
);

-- auto-generated definition
create table tb_drools_modify_history
(
    id               bigint unsigned auto_increment comment '룰 변경이력 고유 ID'
        primary key,
    rule_id          bigint unsigned not null comment '룰 ID',
    rule_action_type varchar(50)     not null comment '룰 변경 타입(UPDATE,DELETE,CREATE)',
    updated_by       varchar(255)    not null comment '룰 수정자',
    updated_at       timestamp       not null comment '룰 수정일',
    rule_contents    longtext        null comment '추가/변경/삭제 된 RULE',
    constraint tb_drools_modify_history_rule_id_foreign
        foreign key (rule_id) references tb_drools (id)
);


