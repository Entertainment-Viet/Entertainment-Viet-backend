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
      <p>To continue creating your new VietEntertainment account, please verify your email address by clicking the button below:
      <br />
          <a href="${verificationUrl}">
              <button >
                  Click Here
              </button>
          </a>
      </p>
      <br />

      <p>If you didn't recently attempt to create a new account with this email address, you can safely disregard this email.</p>
      <br />

      <p>Sincerely,</p>
      <p>
        <em>VietEntertainment Team</em> <br />
      </p>
    </body>
</html>