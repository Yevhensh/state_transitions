CREATE TABLE transitions_matrix
(
    state_id    serial primary key,
    state_name  varchar(255) not null unique,
    transitions varchar[]    not null default []::varchar[]
);

CREATE TABLE entities
(
    entity_id   serial primary key,
    entity_name varchar(255) not null,
    state_name  varchar(255),
    constraint state_name_by_fkey FOREIGN KEY references transitions_matrix(state_name) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE state_change_log
(
    transition_id serial primary key,
    entity_id     int not null,
    from_value    varchar(255),
    to_value      varchar(255),
    constraint from_by_fkey FOREIGN KEY (from_value) references transitions_matrix (state_name) ON UPDATE NO ACTION ON DELETE NO ACTION,
    constraint to_by_fkey FOREIGN KEY (to_value) references transitions_matrix (state_name) ON UPDATE NO ACTION ON DELETE NO ACTION,
    constraint entity_by_fkey FOREIGN KEY (entity_id) references entities (entity_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

