package com.redhat.custom.storage.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
	protected UserEntity entity;
	protected String keycloakId;

	public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model,
			UserEntity entity) {
		super(session, realm, model);
		this.entity = entity;
		keycloakId = StorageId.keycloakId(model, entity.getId());
	}

	@Override
	public String getUsername() {
		return entity.getLoginName();
	}

	@Override
	public void setUsername(String username) {
		entity.setLoginName(username);
	}

	@Override
	public String getFirstName() {
		return entity.getFullName();
	}

	@Override
	public void setFirstName(String fullName) {
		entity.setFullName(fullName);
	}

	@Override
	public void setEmail(String email) {
		entity.setEmail(email);
	}

	@Override
	public String getEmail() {
		return entity.getEmail();
	}

	@Override
	public String getId() {
		return keycloakId;
	}

	@Override
	public void setSingleAttribute(String name, String value) {
		if (name.equals("phone")) {
			entity.setPhone(value);
		} else {
			super.setSingleAttribute(name, value);
		}
	}

	@Override
	public void removeAttribute(String name) {
		if (name.equals("phone")) {
			entity.setPhone(null);
		} else {
			super.removeAttribute(name);
		}
	}

	@Override
	public void setAttribute(String name, List<String> values) {
		if (name.equals("phone")) {
			entity.setPhone(values.get(0));
		} else {
			super.setAttribute(name, values);
		}
	}

	@Override
	public String getFirstAttribute(String name) {
		if (name.equals("phone")) {
			return entity.getPhone();
		} else {
			return super.getFirstAttribute(name);
		}
	}

	@Override
	public Map<String, List<String>> getAttributes() {
		Map<String, List<String>> attrs = super.getAttributes();
		MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
		all.putAll(attrs);
		all.add("phone", entity.getPhone());
		return all;
	}

	@Override
	public List<String> getAttribute(String name) {
		if (name.equals("phone")) {
			List<String> phone = new LinkedList<>();
			phone.add(entity.getPhone());
			return phone;
		} else {
			return super.getAttribute(name);
		}
	}
}
