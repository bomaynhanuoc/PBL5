import datetime
from aiproject.firebase_config import auth


def refresh_token(user):
    # user will have createdAt in idToken field
    # make the time smaller than the real time of expires token
    time_to_expires_token = (int(user['expiresIn']) // 60) - 10
    idToken_start_time = user['idTokenCreatedAt']
    now = datetime.datetime.now()
    # # # convert to minutes
    time_has_passed = (now - idToken_start_time).seconds // 60

    print(time_has_passed)
    print(time_to_expires_token)

    # compare to refresh token
    if time_has_passed == time_to_expires_token:
        user = auth.refresh(user['refreshToken'])
        # update the time for the next refresh
        user['idTokenCreatedAt'] = datetime.datetime.now()
