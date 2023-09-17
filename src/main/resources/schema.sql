create table "persons"
(
    id    int not null,
    email varchar(255),
    login varchar(255),
    name  varchar(255),
    birthday timestamp,
    friendship boolean,
    friends int
);
create unique index user_id_uindex
    on "person" (id);

create table "films"
(
    id int not null,
    mpa varchar(255),
    genre varchar(255),
    name varchar(255),
    description varchar(200),
    release_date timestamp,
    duration real,
    likes int
);
create unique index user_id_uindex
    on "film" (id);

create table "likes"(
    film_id INTEGER REFERENCES films (id),
    user_id INTEGER REFERENCES persons (id)
)