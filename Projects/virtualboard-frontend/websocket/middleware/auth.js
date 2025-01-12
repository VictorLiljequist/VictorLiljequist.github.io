require('dotenv').config()
const jwt = require('jsonwebtoken')

module.exports = (ws, req) => {
    try {
        const authHeader = new URLSearchParams(req.url.slice(1));
        //console.log(authHeader);
        console.log(`Users JWT_TOKEN: ${authHeader.get('token')}`)
        const token = authHeader.get('token')

        if (!token) {
            throw new Error('Token not provided');
        }

        const userData = jwt.verify(token, process.env.JWT_SECRET)
        console.log(`Token authorized for user id: ${userData.sub} name: ${userData.name}`)

        ws.userData = userData

    } catch (error) {
        console.log('Authorization error:', error.message);

        ws.send(JSON.stringify({
            message: "Authorization error",
            error: error.message
        }));

        return ws.close(1008, 'Authorization failed');
    }

}
