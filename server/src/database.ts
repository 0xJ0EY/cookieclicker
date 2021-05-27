import * as FirebaseAdmin from 'firebase-admin';

var serviceAccount = require("../serviceAccountKey.json") as any;

FirebaseAdmin.initializeApp({
    credential: FirebaseAdmin.credential.cert(serviceAccount),
    databaseURL: "https://osrscookieclicker-default-rtdb.europe-west1.firebasedatabase.app/"
});

export const database = FirebaseAdmin.database();