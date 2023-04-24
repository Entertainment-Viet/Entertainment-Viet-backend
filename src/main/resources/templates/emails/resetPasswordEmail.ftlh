<!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Email Verification</title>
    </head>
    <body>
      <p>Hi ${recipientName},</p>
      <br />
      <p>You requested a password change for your account. To reset your password, please click on the button below:
      <br />
          <a href="${verificationUrl}">
              <button >
                  Click Here
              </button>
          </a>
      </p>
      <br />

      <p>If you didn't request a password change, this request is suspicious. To protect your privacy, you should log in and reset your password.</p>
      <br />

      <p>Sincerely,</p>
      <p>
        <em>VietEntertainment Team</em> <br />
      </p>
    </body>
</html>