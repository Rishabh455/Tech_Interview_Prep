# Angular HTTP Interceptor and Route Guards — Interview Revision Notes

> Simple definitions, differences, and practical examples for revision.

## Table of Contents

1. [HTTP Interceptor](#1-http-interceptor)
2. [Auth Guard](#2-auth-guard)
3. [HTTP Interceptor vs Auth Guard](#3-http-interceptor-vs-auth-guard)
4. [Angular Route Guards](#4-angular-route-guards)
   - [CanActivate](#41-canactivate)
   - [CanActivateChild](#42-canactivatechild)
   - [CanDeactivate](#43-candeactivate)
   - [CanMatch](#44-canmatch)
   - [Resolve](#45-resolve-route-resolver-not-a-guard)
5. [How They Work Together](#5-how-they-work-together)
6. [Important Interview Questions](#6-important-interview-questions)
7. [Quick Revision Table](#7-quick-revision-table)

---

## 1. HTTP Interceptor

### Definition

An **HTTP interceptor** intercepts outgoing HTTP requests and incoming HTTP responses made through Angular's `HttpClient`.

### Common uses

- Attach an access token (such as a JWT) to API requests.
- Add common HTTP headers.
- Handle common HTTP errors, such as `401 Unauthorized`.
- Log requests and responses.
- Show or hide a global loading indicator.

### Example: Functional authentication interceptor

This example uses modern Angular's functional interceptor API.

```typescript
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req);
};
```

### Explanation

1. Read the token from storage.
2. If a token exists, clone the request and add the `Authorization` header.
3. Pass the request to `next(req)` so the request can continue.
4. The backend must still validate the token.

Angular HTTP requests are immutable, so use `req.clone()` to modify request properties.

### Register the interceptor

For a standalone Angular application, register it using `provideHttpClient`:

```typescript
import { ApplicationConfig } from '@angular/core';
import {
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';
import { authInterceptor } from './auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
};
```

> Add the interceptor to your existing application configuration rather than creating a second configuration unnecessarily.

### Interview answer

“An HTTP interceptor is used to intercept HTTP requests and responses globally. For example, I can attach a JWT access token to outgoing API requests and handle common HTTP errors in one place.”

---

## 2. Auth Guard

### Definition

An **authentication guard** is a route guard that decides whether navigation to a route should be allowed based on an authentication check.

### Common uses

- Protect dashboard or profile routes.
- Redirect unauthenticated users to the login page.
- Check whether the user meets a route's access requirements.

### Example: Functional `CanActivate` guard

```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (token) {
    return true;
  }

  return router.createUrlTree(['/login']);
};
```

### Protect a route

```typescript
import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { LoginComponent } from './login.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  }
];
```

### Explanation

- If the token exists, the guard returns `true`, allowing navigation.
- If the token does not exist, the guard returns a `UrlTree` for `/login`, redirecting the user.
- Checking whether a token merely exists is a simple demonstration, not a complete security check. The token can be expired or invalid.

### Interview answer

“An Auth Guard controls access to Angular routes. For example, I can use `CanActivate` to allow authenticated users to open the dashboard and redirect other users to the login page.”

---

## 3. HTTP Interceptor vs Auth Guard

| Feature | HTTP Interceptor | Auth Guard |
|---|---|---|
| Main purpose | Intercepts HTTP requests and responses | Controls route navigation |
| Works at | HTTP communication level | Angular Router level |
| Common example | Add a JWT to an API request | Protect `/dashboard` |
| Runs when | An HTTP request or response passes through it | A navigation triggers the relevant route check |
| Typical return | An observable HTTP event stream | A boolean, `UrlTree`, or supported asynchronous result |
| Prevents unauthorized API access by itself? | No | No |
| Requires backend security? | Yes | Yes |

### Easy way to remember

- **Interceptor = API request/response.**
- **Guard = route navigation.**

They solve different problems and are often used together.

---

## 4. Angular Route Guards

Route guards allow an Angular application to control whether a navigation can proceed.

Modern Angular commonly uses **functional guards**. A guard can return a boolean, a `UrlTree`, or an asynchronous result such as a `Promise` or `Observable` of an allowed result.

### 4.1 `CanActivate`

**Definition:** Checks whether a user can activate (enter) a route.

**Use case:** Protect the dashboard so only authenticated users can navigate to it.

```typescript
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  return token
    ? true
    : router.createUrlTree(['/login']);
};
```

Route configuration:

```typescript
{
  path: 'dashboard',
  component: DashboardComponent,
  canActivate: [authGuard]
}
```

**Remember:** `CanActivate` asks, “Can the user enter this route?”

---

### 4.2 `CanActivateChild`

**Definition:** Checks whether a user can activate a child route under a parent route.

**Use case:** Protect child pages under an admin section.

```typescript
import { CanActivateChildFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const childGuard: CanActivateChildFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  return token
    ? true
    : router.createUrlTree(['/login']);
};
```

Route configuration:

```typescript
{
  path: 'admin',
  component: AdminLayoutComponent,
  canActivateChild: [childGuard],
  children: [
    { path: 'users', component: UsersComponent },
    { path: 'reports', component: ReportsComponent }
  ]
}
```

The guard checks navigation to child routes such as `/admin/users` and `/admin/reports`.

**Remember:** `CanActivateChild` asks, “Can the user enter this child route?”

---

### 4.3 `CanDeactivate`

**Definition:** Checks whether a user can leave an active route.

**Use case:** Warn the user when a form has unsaved changes.

Example component:

```typescript
export class EditComponent {
  hasUnsavedChanges(): boolean {
    // Replace this with your actual form-change check.
    return true;
  }
}
```

Guard:

```typescript
import { CanDeactivateFn } from '@angular/router';

export const unsavedGuard: CanDeactivateFn<EditComponent> =
  (component) => {
    return component.hasUnsavedChanges()
      ? confirm('You have unsaved changes. Leave this page?')
      : true;
  };
```

Route configuration:

```typescript
{
  path: 'edit',
  component: EditComponent,
  canDeactivate: [unsavedGuard]
}
```

**How it works:**

- If there are no unsaved changes, the guard returns `true`.
- If there are unsaved changes, the confirmation dialog asks whether to leave.
- If the user selects OK, navigation proceeds; if the user selects Cancel, navigation is blocked.

In a real application, connect `hasUnsavedChanges()` to the form's actual dirty state.

**Remember:** `CanDeactivate` asks, “Can the user leave this route?”

---

### 4.4 `CanMatch`

**Definition:** Checks whether a route is eligible to match a URL. If it returns `false`, Angular can try another matching route.

**Use case:** Select a route based on a role or feature flag.

```typescript
import { CanMatchFn } from '@angular/router';

export const adminMatchGuard: CanMatchFn = () => {
  return localStorage.getItem('role') === 'admin';
};
```

Route configuration:

```typescript
{
  path: 'admin',
  component: AdminComponent,
  canMatch: [adminMatchGuard]
}
```

**Remember:** `CanMatch` asks, “Should this route match this URL?”

> Returning `false` from `CanMatch` does not automatically mean the user will be redirected to the login page. Angular may try another matching route. Add an explicit redirect strategy if needed.

---

### 4.5 `Resolve` (Route Resolver, not a guard)

**Definition:** A route resolver prepares or fetches data before Angular activates the destination route.

**Use case:** Fetch user details before displaying the user page.

Resolver:

```typescript
import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from './user.service';
import { User } from './user.model';

export const userResolver: ResolveFn<User> = () => {
  return inject(UserService).getUser();
};
```

Route configuration:

```typescript
{
  path: 'user',
  component: UserComponent,
  resolve: {
    user: userResolver
  }
}
```

Read the resolved data in the component:

```typescript
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user',
  standalone: true,
  template: `<p>User page</p>`
})
export class UserComponent {
  private route = inject(ActivatedRoute);

  user = this.route.snapshot.data['user'];
}
```

This assumes `UserService.getUser()` returns an observable or promise of a `User`. The component can then use `user` to display the resolved information.

**Remember:** A resolver asks, “What data should be ready before the route loads?”

---

## 5. How They Work Together

Consider a user opening the dashboard:

1. The user navigates to `/dashboard`.
2. `CanActivate` checks whether navigation is allowed.
3. If allowed, Angular activates the dashboard component.
4. The component calls an API using `HttpClient`.
5. The HTTP interceptor adds the access token to the request.
6. The backend validates the token and checks the user's permissions.
7. The backend returns a response, or an error if the request is not authorized.

### Important security point

Angular guards and interceptors run in the browser and can be bypassed or modified by a user. They improve application behavior but are **not a security boundary**. The backend must validate authentication and enforce authorization on every protected API.

Also, a token stored in `localStorage` can be exposed if the application has an XSS vulnerability. Choose token storage and session design based on your application's security requirements.

---

## 6. Important Interview Questions

### Q1. What is an HTTP interceptor?

An HTTP interceptor intercepts outgoing HTTP requests and incoming responses. It is commonly used to add authentication tokens, set common headers, and handle HTTP errors.

### Q2. What is an Auth Guard?

An Auth Guard controls whether a user can navigate to a protected route based on an authentication or authorization check.

### Q3. What is the difference between an interceptor and a guard?

An interceptor works with HTTP requests and responses, while a guard controls route navigation.

### Q4. What is `CanActivate`?

`CanActivate` checks whether a route can be activated. For example, it can protect a dashboard from unauthenticated users.

### Q5. What is `CanActivateChild`?

`CanActivateChild` checks whether a user can activate child routes under a parent route.

### Q6. What is `CanDeactivate`?

`CanDeactivate` checks whether a user can leave a route. It is useful for warning about unsaved form changes.

### Q7. What is `CanMatch`?

`CanMatch` checks whether a route should match a URL. If it returns `false`, Angular can try another matching route.

### Q8. What is a route resolver?

A resolver prepares data before the destination route is activated. It is configured with `resolve` in the route definition.

### Q9. Does `CanActivate` secure backend APIs?

No. It only controls navigation in the Angular application. The backend must independently validate the user's identity and permissions.

### Q10. Can an interceptor redirect to login on a `401` response?

Yes. An interceptor can handle a `401 Unauthorized` response and initiate a login redirect. In a real application, avoid redirect loops and distinguish expired sessions from other errors as needed.

---

## 7. Quick Revision Table

| Feature | Main question it answers | Example |
|---|---|---|
| HTTP Interceptor | How should HTTP requests/responses be handled? | Add a JWT header |
| `CanActivate` | Can the user enter this route? | Protect dashboard |
| `CanActivateChild` | Can the user enter this child route? | Protect admin pages |
| `CanDeactivate` | Can the user leave this route? | Warn about unsaved changes |
| `CanMatch` | Should this route match the URL? | Role-based route selection |
| `Resolve` | What data must be prepared before activation? | Fetch user details |

### One-line memory trick

- **Interceptor:** handles API traffic.
- **CanActivate:** enter a route.
- **CanActivateChild:** enter a child route.
- **CanDeactivate:** leave a route.
- **CanMatch:** match a route.
- **Resolve:** prepare route data.

---

## Final Interview Summary

“An HTTP interceptor handles common HTTP request and response logic, such as attaching JWT tokens and processing errors. Route guards control navigation. `CanActivate` protects a route, `CanActivateChild` protects child routes, `CanDeactivate` checks whether a user can leave a route, and `CanMatch` decides whether a route is eligible to match. A route resolver fetches data before route activation. These client-side mechanisms improve user experience, but the backend must enforce real security.”
