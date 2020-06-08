package com.redhat.custom.storage.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */
@Stateful
@SuppressWarnings("unchecked")

public class CustomUserStorageProvider implements ILocalCustomUserStorageProvider {
	private static final Logger logger = Logger.getLogger(CustomUserStorageProvider.class);

	@PersistenceContext
	protected EntityManager em;

	protected ComponentModel model;

	protected KeycloakSession kcSession;

	@Override
	public void setModel(ComponentModel model) {
		this.model = model;
	}

	@Override
	public void setSession(KeycloakSession session) {
		this.kcSession = session;
	}

	@Override
	public void preRemove(RealmModel realm) {

	}

	@Override
	public void preRemove(RealmModel realm, GroupModel group) {

	}

	@Override
	public void preRemove(RealmModel realm, RoleModel role) {

	}

	@Remove
	@Override
	public void close() {
	}

	/**
	 * Get the raw result from the output of the SELECT SQL and put it into an defined POJO -
	 * UserEntity * @param userEntity
	 * 
	 * @return
	 */
	private UserEntity prepareUserEntity(final Object[] userEntity) {
		return new UserEntity((String) userEntity[0], (String) userEntity[1],
				(String) userEntity[2], (String) userEntity[3]);
	}

	@Override
	public UserModel getUserById(String id, RealmModel realm) {
		logger.info("getUserById: " + id);
		String persistenceId = StorageId.externalId(id);

		Query query = em.createNativeQuery(UserStoreQueries.GET_USER_BY_NAME);
		query.setParameter(1, persistenceId);

		Object[] result = (Object[]) query.getSingleResult();

		if (result == null) {
			logger.info("Could not find user by id: " + id);
			return null;
		}
		return new UserAdapter(kcSession, realm, model, prepareUserEntity(result));
	}

	@Override
	public UserModel getUserByUsername(String username, RealmModel realm) {
		logger.info("getUserByUsername: " + username);

		Query query = em.createNativeQuery(UserStoreQueries.GET_USER_BY_NAME);
		query.setParameter(1, username.toUpperCase());

		List<Object[]> result = query.getResultList();
		if (result == null || result.isEmpty()) {
			logger.info("Could not find user by username: " + username);
			return null;
		}

		return new UserAdapter(kcSession, realm, model, prepareUserEntity(result.get(0)));
	}

	/**
	 * Not implemented.
	 */
	@Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
		return null;
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		logger.info("supportsCredentialType: " + CredentialModel.PASSWORD.equals(credentialType));
		return CredentialModel.PASSWORD.equals(credentialType);
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		logger.info("isConfiguredFor: " + (supportsCredentialType(credentialType)));
		return supportsCredentialType(credentialType);
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
		if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel))
			return false;
		UserCredentialModel cred = (UserCredentialModel) input;

		Query query = em.createNativeQuery(UserStoreQueries.AUTH_USER_PASS);
		query.setParameter(1, cred.getValue());
		query.setParameter(2, user.getUsername());

		List<Object[]> result = query.getResultList();

		if (result == null || result.isEmpty()) {
			logger.info("Authentication failed for the user: " + user.getUsername());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int getUsersCount(RealmModel realm) {
		Object count = em.createNativeQuery(UserStoreQueries.GET_USER_COUNT).getSingleResult();
		return ((Number) count).intValue();
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm) {
		return getUsers(realm, -1, -1);
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {

		Query query = em.createNativeQuery(UserStoreQueries.GET_ALL_USERS);
		if (firstResult != -1) {
			query.setFirstResult(firstResult);
		}
		if (maxResults != -1) {
			query.setMaxResults(maxResults);
		}
		List<Object[]> results = query.getResultList();
		List<UserModel> users = new LinkedList<>();
		for (Object[] entity : results)
			users.add(new UserAdapter(kcSession, realm, model, prepareUserEntity(entity)));
		return users;
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm) {
		return searchForUser(search, realm, -1, -1);
	}

	@Override
	public List<UserModel> searchForUser(String userName, RealmModel realm, int firstResult,
			int maxResults) {
		logger.info("seach user by user name: " + userName);

		Query query = em.createNativeQuery(UserStoreQueries.SEARCH_USER_BY_NAME);
		query.setParameter(1, "%" + userName + "%");

		List<Object[]> result = query.getResultList();
		if (result == null || result.isEmpty()) {
			logger.info("No matching username for: " + userName);
			return Collections.emptyList();
		}

		List<UserModel> userList = new ArrayList<UserModel>();
		for (Object[] userEntity : result) {
			userList.add(new UserAdapter(kcSession, realm, model, prepareUserEntity(userEntity)));
		}
		return userList;
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm,
			int firstResult, int maxResults) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult,
			int maxResults) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue,
			RealmModel realm) {
		return Collections.emptyList();
	}
}
