/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/7/2 18:05:26                            */
/*==============================================================*/
use air_system;

drop table if exists tb_airport;

drop table if exists tb_airroute;

drop table if exists tb_city;

drop table if exists tb_flight;

drop table if exists tb_flights;

drop table if exists tb_mdsp;

drop table if exists tb_model;

drop table if exists tb_order;

drop table if exists tb_passenger;

drop table if exists tb_rule;

drop table if exists tb_space;

drop table if exists tb_ticket;

drop table if exists tb_tickets;

drop table if exists tb_user;

drop table if exists tb_userpass;

/*==============================================================*/
/* Table: tb_airport                                            */
/*==============================================================*/
create table tb_airport
(
   airport_id           bigint auto_increment not null comment '�������',
   city_id              bigint comment '���б��',
   airport_name         varchar(10) comment '��������',
   primary key (airport_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_airport comment '����';

/*==============================================================*/
/* Table: tb_airroute                                           */
/*==============================================================*/
create table tb_airroute
(
   airroute_id          bigint auto_increment not null comment '���߱��',
   airport_up           bigint comment '��ɻ���',
   airport_down         bigint comment '�յ����',
   airroute_length      smallint comment '���߳��ȣ����',
   primary key (airroute_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_airroute comment '����';

/*==============================================================*/
/* Table: tb_city                                               */
/*==============================================================*/
create table tb_city
(
   city_id              bigint auto_increment not null comment '���б��',
   city_name            varchar(10) comment '��������',
   primary key (city_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_city comment '����';

/*==============================================================*/
/* Table: tb_flight                                             */
/*==============================================================*/
create table tb_flight
(
   flight_id            varchar(15) not null comment 'ִ�ɺ�����',
   flights_id           char(6) comment '�����',
   flight_date          date comment 'ִ������',
   flight_state         char(3) comment '����״̬',
   flight_info          varchar(20) comment '������Ϣ',
   pre_upTime           time comment 'Ԥ�����ʱ��',
   pre_downTime         time comment 'Ԥ�Ƶ���ʱ��',
   act_upTime           time comment 'ʵ�����ʱ��',
   act_downTime         time comment 'ʵ�ʵ���ʱ��',
   primary key (flight_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_flight comment 'ִ�ɺ���';

/*==============================================================*/
/* Table: tb_flights                                            */
/*==============================================================*/
create table tb_flights
(
   flights_id           char(6) not null comment '�����',
   model_id             bigint comment '����',
   airroute_id          bigint comment '����',
   flights_meals        char(5) comment '��ʳ',
   plan_upTime          time comment '�ƻ����ʱ��',
   plan_downTime        time comment '�ƻ�����ʱ��',
   plan_time            smallint comment '�ƻ�����ʱ��',
   primary key (flights_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_flights comment '����';

/*==============================================================*/
/* Table: tb_mdsp                                               */
/*==============================================================*/
create table tb_mdsp
(
   model_id             bigint not null comment '����',
   space_id             char(1) not null comment '��λ',
   nums                 smallint comment '����',
   primary key (model_id, space_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_mdsp comment '����-��λ';

/*==============================================================*/
/* Table: tb_model                                              */
/*==============================================================*/
create table tb_model
(
   model_id             bigint auto_increment not null comment '���ͱ��',
   model_name           varchar(8) comment '��������',
   model_speed          smallint comment '�ٶȣ�����/Сʱ��',
   primary key (model_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_model comment '����';

/*==============================================================*/
/* Table: tb_order                                              */
/*==============================================================*/
create table tb_order
(
   order_id             varchar(20) not null comment '�������', 
   user_id              bigint comment '�û����',
   order_price          smallint comment '�۸�',
   order_sureTime       datetime comment 'ȷ��ʱ��',
   order_payTime		datetime comment '֧��ʱ��',
   order_payType		varchar(10) comment '֧����ʽ',
   order_nums           smallint comment '�˿�������',
   order_state          varchar(5) comment '����״̬',
   order_contactName    varchar(10) comment '��ϵ������',
   order_contactTel     varchar(20) comment '��ϵ���ֻ���',
   order_contactEmail   varchar(20) comment '��ϵ������',
   primary key (order_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_order comment '����';

/*==============================================================*/
/* Table: tb_passenger                                          */
/*==============================================================*/
create table tb_passenger
(
   passenger_id         bigint auto_increment not null comment '�˿��˱��',
   ticket_id            varchar(20) comment '��Ʊ���',
   passenger_name       varchar(10) comment '�˿�������',
   passenger_cerid      varchar(30) comment '֤����',
   passenger_certype    varchar(10) comment '֤������',
   passenger_tel        varchar(20) comment '�˿����ֻ�����',
   passenger_type       char(5) comment '�˿�����',
   primary key (passenger_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_passenger comment '�˻�����Ϣ';

/*==============================================================*/
/* Table: tb_rule                                               */
/*==============================================================*/
create table tb_rule
(
   space_id             char(1) not null comment '��λ���',
   rule_type            char(1) not null comment '�˸�����',
   rule_refund          smallint comment '��Ʊ��������',
   rule_change          smallint comment '��Ʊ��������',
   primary key (space_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_rule comment '�˸Ĺ���';

/*==============================================================*/
/* Table: tb_space                                              */
/*==============================================================*/
create table tb_space
(
   space_id             char(1) not null comment '��λ���',
   space_name           varchar(8) comment '��λ����',
   baggage_allowance    smallint comment '�����޶�',
   primary key (space_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_space comment '��λ';

/*==============================================================*/
/* Table: tb_ticket                                             */
/*==============================================================*/
create table tb_ticket
(
   ticket_id            varchar(20) not null comment '��Ʊ���',
   tickets_id           varchar(20) comment '��Ʊ���',
   order_id             varchar(20) comment '�������',
   ticket_price         smallint comment 'Ʊ��',
   fuel_surcharge       smallint comment '���������',
   airport_fee          smallint comment '���ձ��շ�',
   primary key (ticket_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_ticket comment '��Ʊ';

/*==============================================================*/
/* Table: tb_tickets                                            */
/*==============================================================*/
create table tb_tickets
(
   tickets_id           varchar(20) not null comment '��Ʊ���', 
   space_id             char(1) comment '��λ���',
   flight_id            varchar(15) comment 'ִ�ɺ�����',
   tickets_price        smallint comment 'Ʊ��',
   tickets_nums         smallint comment '��Ʊ��',
   primary key (tickets_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_tickets comment '������Ʊ';

/*==============================================================*/
/* Table: tb_user                                               */
/*==============================================================*/
create table tb_user
(
   user_id              bigint auto_increment not null comment '�û����',
   user_tel 			varchar(20) not null unique comment '�ֻ���',
   user_cerid           varchar(30) not null unique comment '֤����',
   user_certype         varchar(10) comment '֤������',
   user_chname          varchar(10) comment '������',
   user_enname          varchar(30) comment 'Ӣ����',
   user_salt            varchar(100) comment '��ֵ',
   user_pass            varchar(100) comment '����',
   primary key (user_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_user comment '�û�';

/*==============================================================*/
/* Table: tb_userpass                                           */
/*==============================================================*/
create table tb_userpass
(
   userpass_id          bigint auto_increment not null comment '���ó˻��˱��',
   user_id              bigint comment '�û����',
   userpass_name        varchar(10) comment '�˻�������', 
   userpass_cerid       varchar(30) comment '�˻���֤����',
   userpass_certype     varchar(10) comment '�˻���֤������',
   userpass_tel         varchar(20) comment '�˻����ֻ���',
   userpass_type        char(5) comment '�˻�������',
   primary key (userpass_id)
)CHARSET=utf8 ENGINE=InnoDB;

alter table tb_userpass comment '���ó˻���';

alter table tb_airport add constraint FK_Relationship_1 foreign key (city_id)
      references tb_city (city_id) on delete restrict on update restrict;

alter table tb_airroute add constraint FK_Relationship_2 foreign key (airport_down)
      references tb_airport (airport_id) on delete restrict on update restrict;

alter table tb_airroute add constraint FK_Relationship_3 foreign key (airport_up)
      references tb_airport (airport_id) on delete restrict on update restrict;

alter table tb_flight add constraint FK_Relationship_5 foreign key (flights_id)
      references tb_flights (flights_id) on delete restrict on update restrict;

alter table tb_flights add constraint FK_Relationship_4 foreign key (airroute_id)
      references tb_airroute (airroute_id) on delete restrict on update restrict;

alter table tb_flights add constraint FK_Relationship_6 foreign key (model_id)
      references tb_model (model_id) on delete restrict on update restrict;

alter table tb_mdsp add constraint FK_tb_mdsp foreign key (model_id)
      references tb_model (model_id) on delete restrict on update restrict;

alter table tb_mdsp add constraint FK_tb_mdsp2 foreign key (space_id)
      references tb_space (space_id) on delete restrict on update restrict;

alter table tb_order add constraint FK_Relationship_10 foreign key (user_id)
      references tb_user (user_id) on delete restrict on update restrict;

alter table tb_passenger add constraint FK_Relationship_12 foreign key (ticket_id)
      references tb_ticket (ticket_id) on delete restrict on update restrict;

alter table tb_rule add constraint FK_Relationship_7 foreign key (space_id)
      references tb_space (space_id) on delete restrict on update restrict;

alter table tb_ticket add constraint FK_Relationship_11 foreign key (tickets_id)
      references tb_tickets (tickets_id) on delete restrict on update restrict;

alter table tb_ticket add constraint FK_Relationship_14 foreign key (order_id)
      references tb_order (order_id) on delete restrict on update restrict;

alter table tb_tickets add constraint FK_Relationship_8 foreign key (flight_id)
      references tb_flight (flight_id) on delete restrict on update restrict;

alter table tb_tickets add constraint FK_Relationship_9 foreign key (space_id)
      references tb_space (space_id) on delete restrict on update restrict;

alter table tb_userpass add constraint FK_Relationship_13 foreign key (user_id)
      references tb_user (user_id) on delete restrict on update restrict;

create view v_airroute_airport_city(airroute_id,airportUpName,airportDownName,cityStartName,cityEndName,airroute_length)
as
select airroute_id ,x.airport_name,y.airport_name,a.city_name,b.city_name,airroute_length
from tb_airport x,tb_airport y,tb_city a,tb_city b ,tb_airroute
where tb_airroute.airport_up = x.airport_id
and tb_airroute.airport_down = y.airport_id
and x.city_id = a.city_id
and y.city_id = b.city_id  ;

select * from v_airroute_airport_city;

alter table tb_city
    add  column city_alp char(1) not null ;/*��ӳ�������ĸ*/

alter table tb_city
    add  constraint  unique_name unique(city_name);

alter table tb_ticket
    add column ticket_seat int;/*�����λ��*/

alter table tb_passenger
    modify column passenger_certype varchar(20) ;

alter table tb_user
    modify column user_certype varchar(20) ;

alter table tb_userpass
    modify column userpass_certype varchar(20) ;