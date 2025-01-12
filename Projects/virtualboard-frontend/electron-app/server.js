const WebSocket = require('ws')
const authorize = require('./middleware/auth')
require('dotenv').config()

const PORT = process.env.PORT || 8080;
const wss = new WebSocket.Server({ port: PORT });
const clients = {};


wss.on('connection', (ws, req) => {
    // Authorize user, if not authorized then close the connection
    authorize(ws, req);

    if (!ws.userData) {
        console.log("Not Authorized");
        ws.on('close', () => {
            console.log("Client disconnected");
        })
        return;
    }

    //console.log(req.headers)
    console.log(`Client connected: ${req.headers['sec-websocket-key']}`);

    // Get the jwt_token and board_id
    const urlParams = new URLSearchParams(req.url.slice(1));
    const jwt_token = urlParams.get('token')
    const board_id = urlParams.get('board_id')

    //console.log("JWT_TOKEN: " + jwt_token);
    console.log("BOARD_ID: " + board_id);

    // Add's the board_id's to the clients array if it isn't in there already
    if (!clients[board_id]) {
        clients[board_id] = new Set();
    }

    // Add only one user once
    if (!clients[board_id].has(ws)) {
        clients[board_id].add(ws);
    }

    console.log(`Clients connected on board ${board_id}: ${clients[board_id].size}`)

    ws.on('message', async (message) => {

        // Message has all relevant data to be sent to all other clients!

        // Message contains the data sent from the client
        //console.log("Raw message received:", message);

        let receivedData;

        try {
            // Parse the received message assuming it's JSON
            receivedData = JSON.parse(message);
            //console.log("Parsed message data:", receivedData);
        } catch (e) {
            console.error("Error parsing message:", e);
            return ws.send('Invalid data format, expected JSON');
        }

        /*console.log("Received data: ")
        console.log(receivedData)
        console.log("----------------")*/

        const { type, board_id2 } = receivedData;

        if (type === "createCard") {

            //console.log("BI: " + receivedData.boardId + "CI: " + receivedData.id + "Cont: " + receivedData.content)

            for (const boardId in clients) {
                clients[boardId].forEach(client => {
                    // Create the data to send back to the clients
                    if (client === ws) {
                        return;
                    }
                    let cardBody = JSON.stringify({
                        type: "createCard",
                        status: 0,
                        id: receivedData.id,
                        boardId: receivedData.boardId,
                        title: receivedData.title,
                        content: receivedData.content,
                    })
    
                    // Send data to all clients, 
                    client.send(cardBody);
                });
            }

        } else if (type === "updateCard") {
            // Message parsed successfully, now broadcast to all clients in board_id
            clients[board_id].forEach(client => {
                // Create the data to send back to the clients
                if (client === ws) {
                    return;
                }

                let dataToSend = JSON.stringify({
                    type: "updateCard",
                    status: 0,
                    cardId: receivedData.cardId,
                    content: receivedData.content
                });

                // Send data to all clients,
                client.send(dataToSend);
            });
        } else if (type === "deleteCard") {
            // TODO Add delete functionality
            //console.log(`Type is : ${receivedData.type}`)

            clients[board_id].forEach(client => {
                if (client === ws) {
                    return;
                };

                let cardToBeDeleted = JSON.stringify({
                    type: 'deleteCard',
                    id: receivedData.id,
                    boardId: receivedData.boardId
                });

                client.send(cardToBeDeleted);
            })
        } else {
            console.log("No type detected");
        }

        //ws.send('Message received on server and broadcasted to clients');
        ws.send(JSON.stringify({ message: 'Message received on server and broadcasted to clients' }));
    })

    ws.on('close', () => {
        clients[board_id].delete(ws);
        console.log("Client disconnected");
    })

})