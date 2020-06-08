package com.redhat.custom.storage.user;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */

public interface ILocalCustomUserStorageProvider extends UserStorageProvider, UserLookupProvider,
		UserQueryProvider, CredentialInputValidator {

	void setModel(ComponentModel model);

	void setSession(KeycloakSession session);

}
