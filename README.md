# login
Simple login module using the Java Spark Framework

## API
When the API requires you to be logged in or if authentication fails in some way it will return a Status code of `401`.
If there is information missing in a request it will return a Status of `400`.
Internal errors will result in Status of `500`.

### Sign-Up

Request:

`POST /sign-up`

```json
{
  "username":"sam@sam-the-man.com", 
  "password":"test", 
  "info": "Hello World!"
}
```

Response:

```json
Status: 200 OK
{}
```

### Login

Request:

`POST /login`

```json
{
  "username":"sam@sam-the-man.com", 
  "password":"test"
}

```

Response:

```json
Status: 200 OK
{"session":"5b30b1a4-31db-46e4-b359-9e726ccb681b"}
```

### View Profile
This will either return the profile information or a 404

Request:

`GET /profile/:user`

Response:

```json
Status: 200 OK
{
  "username":"sam@sam-the-man.com",
  "info":"Hello World!"
}
```

### Update Profile

Request:

`PUT /profile`

```json
{	
	"username":"sam@sam-the-man.com", 
	"new_info":"Father, Software Engineer", 
	"session":"5b30b1a4-31db-46e4-b359-9e726ccb681b"
}
```

Response:

```json
Status: 200 OK
{}
```

### Change Password
This requires either a session or the current password of the user

`PUT /password`

```json
{	
	"username":"sam@sam-the-man.com", 
	"new_password":"test1", "new_password_confirm": "test1",
	"session":"5b30b1a4-31db-46e4-b359-9e726ccb681b"
}
```

Response:

```json
Status: 200 OK
{}
```

### Delete Profile

Request:

`DELETE /profile`

```json
{	
	"username":"sam@sam-the-man.com", 
	"password":"test", 
	"session":"5b30b1a4-31db-46e4-b359-9e726ccb681b"
}
```

Response:

```json
Status: 200 OK
{}
```
