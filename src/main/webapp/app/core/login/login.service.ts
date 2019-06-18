import { Injectable } from '@angular/core';

import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-session.service';
import { ActivatedRoute } from '@angular/router';

declare var localStorage: any;

@Injectable({ providedIn: 'root' })
export class LoginService {
  private storage: any = localStorage;

  constructor(
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private activatedRoute: ActivatedRoute
  ) {}

  login() {
    // recherche du realm dans le params et stockage
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['realm']) {
        this.saveRealm(params['realm']);
      }
    });

    // récupère le realm du stockage, issu des params ou d'un stockage précédent
    const realm = this.retrieveRealm();
    console.log('current realm to log in', realm);

    const port = location.port ? ':' + location.port : '';
    let contextPath = location.pathname;
    if (contextPath.endsWith('accessdenied')) {
      contextPath = contextPath.substring(0, contextPath.indexOf('accessdenied'));
    }
    if (!contextPath.endsWith('/')) {
      contextPath = contextPath + '/';
    }

    // If you have configured multiple OIDC providers, then, you can update this URL to /login.
    // It will show a Spring Security generated login page with links to configured OIDC providers.
    location.href = `//${location.hostname}${port}${contextPath}oauth2/authorization/${realm}`;
  }

  logout() {
    this.authServerProvider.logout().subscribe(response => {
      const data = response.body;
      let logoutUrl = data.logoutUrl;
      // if Keycloak, uri has protocol/openid-connect/token
      if (logoutUrl.indexOf('/protocol') > -1) {
        logoutUrl = logoutUrl + '?redirect_uri=' + window.location.origin;
      } else {
        // Okta
        logoutUrl = logoutUrl + '?id_token_hint=' + data.idToken + '&post_logout_redirect_uri=' + window.location.origin;
      }
      window.location.href = logoutUrl;
    });
  }

  saveRealm(val): void {
    this.storage.setItem('gpdoccurrentrealm', val);
  }

  retrieveRealm(): string {
    return this.storage.getItem('gpdoccurrentrealm');
  }
}
