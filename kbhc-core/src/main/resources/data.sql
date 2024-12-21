insert into t_role (`role_type`, `deleted`, `created_at`) values ('ADMIN', false, now());
insert into t_role (`role_type`, `deleted`, `created_at`) values ('MEMBER', false, now());
insert into t_role (`role_type`, `deleted`, `created_at`) values ('EXPERT', false, now());

insert into t_user (`email`, `password`, `name`, `nick_name`, `deleted`, `created_at`) values ('admin@admin.com', '$2a$10$o60CnAfUyyo3y5lpjHTO3.JcfYkxuc5/JvzWmzKHZscmjij9Tp5pS', '관리자', '관리자', false, now());
insert into t_user_role (`role_no`, `user_no`) values (1, 1);