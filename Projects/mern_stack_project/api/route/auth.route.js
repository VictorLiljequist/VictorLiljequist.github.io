import express from 'express';
import { signup, signin, google, signOut } from '../controllers/auth.controller.js';
import { updateUser } from '../controllers/user.controller.js';


const router = express.Router();

router.post("/signup", signup)
router.post("/signin", signin)
router.post("/update", updateUser)
router.post("/google", google)
router.get("/signout", signOut)

export default router;