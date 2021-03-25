package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.util.List;

public interface RightsRolesRepository {
    Role findRoleForUser(long userId);

    Role findRoleByTitle(String role);

    Boolean addRoleToUser(User user, Role role);

    Boolean updateRoleToUser(User user);

    Boolean addRole(String role);

    Boolean addRight(String right);

    Right findRightByTitle(String right);

    List<Right> findRightsForRole(Role role);

    Boolean addRoleRight(Long roleId, Long rightId);

    Boolean removeAllRoles();

    Boolean removeAllRights();

    Boolean removeAll();
}
