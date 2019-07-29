# Stream Audio Into a Call with Java

This is the code for the Nexmo Developer blog post of the same name. It shows you how to create a conference and then stream audio into it.

Configure and run the application by executing the following steps:


## Install the Nexmo CLI

This step requires `npm` (the Node Package Manager), which is part of [Node.js](https://nodejs.org/en/).

Run the following command, replacing `NEXMO_API_KEY` and `NEXMO_API_SECRET` with your API key and secret in the [Developer Dashboard](https://dashboard.nexmo.com/settings):

```sh
npm install -g nexmo-cli
nexmo setup NEXMO_API_KEY NEXMO_API_SECRET
```

## Buy a Nexmo Virtual number

Search for available numbers with `voice` capability, using your [two-character country code](https://www.iban.com/country-codes). For example, using `GB` for the United Kingdom:

```sh
nexmo number:search GB --voice --verbose
```

Buy one of the numbers, for example `442079460000`:

```sh
nexmo number:buy 442079460000
```

## Run ngrok

Your webhooks must be publicly accessible over the Internet. You can use `ngrok` for this (see our [blog post on ngrok](https://www.nexmo.com/blog/2017/07/04/local-development-nexmo-ngrok-tunnel-dr)).

Run `ngrok` on port 3000 using the following command and make a note of the URLs it creates for you:

```sh
ngrok http 3000
```

## Create a Voice API application

Run the following command in the project's root directory, replacing the `/webhooks/answer` URL domain name with the one provided by `ngrok`. We don't use `/webhooks/events` in this example, so provide any URL for that. 

```sh
nexmo app:create "Play Call Audio" https://41acbcd0.ngrok.io/webhooks/answer https://example.com/webhooks/events --keyfile private.key
```

Make a note of the application ID. This step also downloads your Nexmo credentials in a file called `private.key`.

## Link your Nexmo number to your Voice Application

Replace `NEXMO_APPLICATION_ID` with the one returned by the preceding command.

```sh
nexmo link:app NEXMO_NUMBER NEXMO_APPLICATION_ID
```

## Configure the application

Copy `example.env` to `.env`. Replace the `NEXMO_APPLICATION_ID` with your own application ID. If you have followed the steps above you can leave the other settings unchanged.

## Run the application

1. Execute `gradle run`.

2. Dial your Nexmo number.

3. When the call is answered, you should hear a short welcome message.

4. Copy the call ID that is shown in the console.

5. In your browser's address bar, enter `http://localhost:3000/play/`, followed by the call ID.

6. The audio at `AUDIO_URL` should be streamed into the call and terminated after the number of milliseconds specified in `AUDIO_DURATION_MSEC`.