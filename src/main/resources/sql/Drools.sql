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


----
CREATE TABLE tb_iot_rule (
                             id               BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 ID',    -- 룰 고유 ID
                             ruleGroup        VARCHAR(100)      NOT NULL COMMENT 'ruleGroup 명',           -- 룰 이름
                             conditions       TEXT              NOT NULL COMMENT '조건 JSON TEXT',           -- 조건(복잡한 경우 JSON으로 저장)
                             actions          TEXT              NOT NULL COMMENT '결과 값 text',           -- 액션(복잡한 경우 JSON으로 저장)
                             priority         INT               DEFAULT 1 COMMENT '우선 순위',          -- 우선순위 (높을수록 먼저 적용)
                             active           BOOLEAN           DEFAULT TRUE COMMENT '사용 여부',       -- 사용/미사용 여부
                             created_by       VARCHAR(255)      NOT NULL COMMENT '등록자',
                             updated_by       VARCHAR(255)      NULL COMMENT '수정자',
                             created_at       DATETIME          DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
                             updated_at       DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'
);

-- 일치 (match / equal to)
INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"string" , "functionName": "temperature", "operator":"MATCH", "functionValue": 25}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 일치 합니다." }',
          1, true,'GIRB_CURRENT_000002', '', now(), now() );

-- 초과 (greater than) GT
INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"default" , "functionName": "temperature", "operator":"GT", "functionValue": 30}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 초과 하였습니다." }',
          2, true,'GIRB_CURRENT_000002', '', now(), now() );

-- 이상 (greater than equal to) GTE
INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"default" , "functionName": "temperature", "operator":"GTE", "functionValue": 28}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 28도 이상 입니다." }',
          3, true,'GIRB_CURRENT_000002', '', now(), now() );

-- 미만 (less than) LT
INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"default" , "functionName": "humidity", "operator":"LT", "functionValue": 40}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 40도 미만 입니다. 습도 조절 하세요." }',
          4, true,'GIRB_CURRENT_000002', '', now(), now() );

-- 이하 ( less than or equal to)
INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"default" , "functionName": "temperature", "operator":"LTE", "functionValue": 20}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 40도 이하 입니다." }',
          5, true,'GIRB_CURRENT_000002', '', now(), now() );


-- 범위 , 내부 (inside) INSIDE
INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"range" , "functionName": "temperature", "operator":"INSIDE", "minValue": 20, "maxValue": 25}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 20~25도 내부" }',
          6, true,'GIRB_CURRENT_000002', '', now(), now() );

INSERT INTO tb_iot_rule(ruleGroup, conditions, actions, priority, active, created_by, updated_by, created_at, updated_at)
values(
          'TemperatureValveRules',
          '{"type" :"range" , "functionName": "temperature", "operator":"OUTSIDE", "minValue": 10, "maxValue": 30}',
          '{ "actionType" : "sendAlert" , "message" :"온도가 10~30도 외부" }',
          7, true,'GIRB_CURRENT_000002', '', now(), now() );