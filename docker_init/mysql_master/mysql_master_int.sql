CREATE DATABASE IF NOT EXISTS spring_dungi;
USE spring_dungi;

CREATE TABLE IF NOT EXISTS Memo (
    memo_id bigint not null auto_increment,
    created_time datetime(6),
    modifed_time datetime(6),
    delete_status varchar(255),
    memo_color varchar(255),
    memo_item varchar(255),
    x_position double precision,
    y_position double precision,
    room_id bigint,
    user_id bigint,
    primary key (memo_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS NoticeVote (
    DTYPE varchar(31) not null,
    notice_vote_id bigint not null auto_increment,
    created_time datetime(6),
    modifed_time datetime(6),
    delete_status varchar(255),
    notice_item varchar(255),
    vote_status varchar(255),
    title varchar(255),
    room_id bigint,
    users_id bigint,
    primary key (notice_vote_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS RepeatDay (
    repeat_day_id bigint not null auto_increment,
    day integer not null,
    todo_id bigint,
    primary key (repeat_day_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS Room (
    room_id bigint not null auto_increment,
    created_time datetime(6),
    modifed_time datetime(6),
    color varchar(255),
    delete_status varchar(255),
    name varchar(255),
    primary key (room_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS Todo (
    DTYPE varchar(31) not null,
    todo_id bigint not null auto_increment,
    created_time datetime(6),
    modifed_time datetime(6),
    dealine datetime(6),
    delete_status varchar(255),
    todo_item varchar(255),
    todo_status varchar(255),
    room_id bigint,
    users_id bigint,
    primary key (todo_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS UserRoom (
    users_room_id bigint not null auto_increment,
    delete_status varchar(255),
    room_id bigint,
    users_id bigint,
    primary key (users_room_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS Users (
    users_id bigint not null auto_increment,
    created_time datetime(6),
    modifed_time datetime(6),
    best_mate_count integer,
    delete_status varchar(255),
    email varchar(255),
    name varchar(255),
    nickname varchar(255),
    password varchar(255),
    phone_number varchar(255),
    profile_img varchar(255),
    provider varchar(255),
    primary key (users_id)
) engine=InnoDB;


CREATE TABLE IF NOT EXISTS UserVoteItem (
    users_vote_item_id bigint not null auto_increment,
    delete_status varchar(255),
    users_id bigint,
    vote_item_id bigint,
    primary key (users_vote_item_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS VoteItem (
    vote_item_id bigint not null auto_increment,
    choice varchar(255),
    notice_vote_id bigint,
    primary key (vote_item_id)
) engine=InnoDB;

    
ALTER TABLE Users 
    add constraint user_idx unique (email);


ALTER TABLE Memo 
    add constraint FKn9tq8318n0nqyr5jdw490iywe 
    foreign key (room_id) 
    references Room (room_id);


ALTER TABLE Memo 
    add constraint FKpgughxrro96n7dtnioh8xvgex 
    foreign key (user_id) 
    references Users (users_id);


ALTER TABLE NoticeVote 
    add constraint FK92wjjpgrbxxanuy2rmfh2mdji 
    foreign key (room_id) 
    references Room (room_id);


ALTER TABLE NoticeVote 
    add constraint FK3bb3ssf7bwc6a2fve1kxakt2a 
    foreign key (users_id) 
    references Users (users_id);


ALTER TABLE RepeatDay 
    add constraint FKa9j014iwb1p0pqx3q6b4gspp3 
    foreign key (todo_id) 
    references Todo (todo_id);


ALTER TABLE Todo 
    add constraint FKcic4sh4f3f5ys0t0p5mdh3swf 
    foreign key (room_id) 
    references Room (room_id);


ALTER TABLE Todo 
    add constraint FK3ur7e46vhq9h0qybemakll7jo 
    foreign key (users_id) 
    references Users (users_id);


ALTER TABLE UserRoom 
    add constraint FK4yjodfn3d4j01tj4agp9f2rr0 
    foreign key (room_id) 
    references Room (room_id);


ALTER TABLE UserRoom 
    add constraint FKppt61nfpuco28nmlv3isoc8j5 
    foreign key (users_id) 
    references Users (users_id);


ALTER TABLE UserVoteItem 
    add constraint FK1yl46hmg9tbgx1tvwxdd91ei0 
    foreign key (users_id) 
    references Users (users_id);


ALTER TABLE UserVoteItem 
    add constraint FKe57be5r40y0blpd2xnufgqvaq 
    foreign key (vote_item_id) 
    references VoteItem (vote_item_id);


ALTER TABLE VoteItem 
    add constraint FKd8q2ybut8qxn76g5fvupohcml 
    foreign key (notice_vote_id) 
    references NoticeVote (notice_vote_id);


SET GLOBAL server_id=3;
SHOW variables LIKE 'server_id';