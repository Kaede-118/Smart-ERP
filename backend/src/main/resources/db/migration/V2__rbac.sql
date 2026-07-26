CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE TABLE sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(20) DEFAULT 'button',
    parent_id BIGINT DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE KEY uk_user_role (user_id, role_id)
);

CREATE TABLE sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    UNIQUE KEY uk_role_permission (role_id, permission_id)
);

-- Seed roles
INSERT INTO sys_role (role_name, role_code, description) VALUES
('超级管理员', 'ADMIN', '拥有所有权限'),
('仓库管理员', 'WAREHOUSE', '管理库存相关业务'),
('销售员', 'SALES', '管理销售相关业务');

-- Seed permissions (user module)
INSERT INTO sys_permission (name, code, type) VALUES
('用户列表', 'user:list', 'button'),
('用户详情', 'user:view', 'button'),
('新增用户', 'user:create', 'button'),
('修改用户', 'user:update', 'button'),
('删除用户', 'user:delete', 'button');

-- Seed permissions (role module)
INSERT INTO sys_permission (name, code, type) VALUES
('角色列表', 'role:list', 'button'),
('角色详情', 'role:view', 'button'),
('新增角色', 'role:create', 'button'),
('修改角色', 'role:update', 'button'),
('删除角色', 'role:delete', 'button');

-- Seed permissions (permission module)
INSERT INTO sys_permission (name, code, type) VALUES
('权限列表', 'perm:list', 'button'),
('权限详情', 'perm:view', 'button'),
('新增权限', 'perm:create', 'button'),
('修改权限', 'perm:update', 'button'),
('删除权限', 'perm:delete', 'button');

-- Grant all permissions to ADMIN role (role_id=1)
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE deleted = 0;

-- Assign ADMIN role (role_id=1) to user admin (user_id=1)
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);
