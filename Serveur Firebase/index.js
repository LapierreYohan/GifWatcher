import { initializeApp } from "firebase-admin/app";
import { getMessaging } from "firebase-admin/messaging";
import express from "express";
import cors from "cors";

import admin from "firebase-admin";
import serviceAccount from "./gifswatcher-1695048501603-firebase-adminsdk-o72jw-b3d29bfe2a.json" assert { type: 'json' }

process.env.GOOGLE_APPLICATION_CREDENTIALS;

const app = express();
app.use(express.json());

app.use(
    cors({
        origin: "*",
    })
)

app.use(
    cors({
        methods: "GET,HEAD,PUT,PATCH,POST,DELETE",
    })
)

app.use((req, res, next) => {
  res.setHeader("Content-Type", "application/json");
  next();
});

initializeApp({
  credential: admin.credential.cert(serviceAccount),
  projectId: "gifswatcher-1695048501603"
});

app.post("/send", async (req, res) => {

    const receivedToken = req.body.fcmToken;
    const notifTitle = req.body.title;
    const notifBody = req.body.body;
    const imageUrl = `https://cdn.discordapp.com/attachments/1057259426232414260/1205452191813337128/loutre_notification.png?ex=65d86bd6&is=65c5f6d6&hm=7f975f4152467b4b17550009ddcde356dd884f96e15f7b4d9eca066ce45df8d0&`;


    if (!receivedToken) {
        res
            .status(400)
            .json(
                { 
                    message: "Token not provided"
                }
            );
        return;
    }

    if (!notifTitle || !notifBody) {
        res
            .status(400)
            .json(
                { 
                    message: "Notification title or body not provided"
                }
            );
        return;
    }

    const message = {
        notification: {
            title: notifTitle,
            body: notifBody,
            image: imageUrl
        },
        token: receivedToken
    };

    getMessaging()
        .send(message)
        .then((response) => {
            res
                .status(200)
                .json(
                    { 
                        message: "Notification sent successfully",
                        token: receivedToken
                    }
                );

            console.log("Successfully sent message:", response);
        })
        .catch((error) => {
            res
                .status(400)
                .send(error)
            console.log("Error sending message:", error);
        });


});

app.listen(3000, () => {
  console.log("Server is running on port 3000");
});