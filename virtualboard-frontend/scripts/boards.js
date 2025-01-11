if (!localStorage.getItem('token')) {
    alert("You are not logged in")
    window.location.href = 'http://127.0.0.1:5501/index.html';
}

//let boards = [];  


// Function to fetch boards from the backend
async function fetchBoards() {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch('http://localhost:8080/boards', {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log("Boards fetched:", data);
        boards = data.boards;  
        console.log("Boards array:", boards); 
        updateBoardsSelector(boards);
    } catch (error) {
        console.error("Error fetching boards:", error);
    }
}

function updateBoardsSelector(boards) {
    const selector = document.getElementById('boards-selector');
    selector.innerHTML = '';
    boards.forEach(board => {
        const option = document.createElement('option');
        option.value = board.id;
        option.textContent = board.title;
        selector.appendChild(option);
    });

    if (boards.length > 0) {
        selector.value = boards[0].id; 
        displayBoardCards(boards[0].cards); 
    }
    
    // Takes the necessary id's, names and such and creates a websocket connection
    const boardName = document.getElementById('boards-selector').options[selector.selectedIndex].text;
    const startBoard = document.getElementById('boards-selector').value;
    console.log("Starting Board: " + startBoard + " " + boardName);
    // WebSocket connection
    connectWebSocket(startBoard, boardName, boards[0].cards);
}


// Function to create a new board
async function createBoard() {
    const title = prompt("Enter board title:");
    const content = prompt("Enter board content:");

    const boardData = {
        title: title,
        content: content,
    };

    const token = localStorage.getItem('token');

    try {
        const response = await fetch('http://localhost:8080/boards', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(boardData),
        });

        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorResponse.msg || "Unknown error"}`);
        }

        const newBoard = await response.json();
        console.log("Board created:", newBoard);

    } catch (error) {
        console.error("Error creating board:", error);
    }
}



// Function to create a new card
async function createCard() {
    const boardId = document.getElementById('boards-selector').value;
    const title = document.getElementById('new-card-name').value;
    const content = document.getElementById('content').value;

    console.log("BoardId: " + boardId)

    const cardData = {
        title: title,
        content: content,
    };

    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`http://localhost:8080/boards/${boardId}/cards`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(cardData),
        });

        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorResponse.msg || "Unknown error"}`);
        }

        const newCard = await response.json();
        console.log("Card created:", newCard);
        hideCardModal();  
        await fetchCardsForBoard(boardId);

    } catch (error) {
        console.error("Error creating card:", error);
    }
}

async function editCard(cardId) {
    const titleElement = document.getElementById(`title-${cardId}`);
    const contentElement = document.getElementById(`content-${cardId}`);

    if (!titleElement || !contentElement) {
        console.error(`Card elements with ID ${cardId} not found.`);
        return;
    }

    const updateCardContent = async () => {
        const newTitle = titleElement.innerText;
        const newContent = contentElement.innerText; 

        const token = localStorage.getItem('token');
        const boardId = document.getElementById('boards-selector').value; 

        const updatedCardData = {
            title: newTitle,   
            content: newContent 
        };

        try {
            const response = await fetch(`http://localhost:8080/boards/${boardId}/cards/${cardId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(updatedCardData), 
            });

            if (!response.ok) {
                const errorResponse = await response.json();
                throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorResponse.msg || "Unknown error"}`);
            }

            console.log("Card updated:", { id: cardId, title: newTitle, content: newContent });

        } catch (error) {
            console.error("Error updating card:", error);
        }
    };

    titleElement.addEventListener('blur', updateCardContent);
    contentElement.addEventListener('blur', updateCardContent);
}


function displayBoardCards(cards, boardId) {
    const cardContainer = document.getElementById('cards-container');
    cardContainer.innerHTML = ''; 

    if (cards.length === 0) {
        cardContainer.innerHTML = '<p>No cards available for this board.</p>';
        return;
    }

    cards.forEach(card => {
        const cardElement = document.createElement('div');
        cardElement.setAttribute('id', card.id);
        cardElement.classList.add('bg-white', 'shadow-md', 'rounded-lg', 'p-4', 'mb-4', 'inline-block', 'm-3');
        cardElement.setAttribute('draggable', 'true');

        cardElement.innerHTML = `
            <h3 class="text-lg font-bold" contenteditable="true" id="title-${card.id}">${card.title || 'Untitled'}</h3>
            <p class="text-gray-600" contenteditable="true" id="content-${card.id}">${card.content || 'No content'}</p>
        `;
        let isDragging = false;
        let startX, startY, initialMouseX, initialMouseY;

        cardElement.addEventListener('mousedown', (event) => {
            const isEditableElement = event.target.closest('[contenteditable="true"]');
            if (isEditableElement) {
                editCard(card.id); 
            }

            isDragging = false; 
            startX = cardElement.offsetLeft;
            startY = cardElement.offsetTop;
            initialMouseX = event.clientX;
            initialMouseY = event.clientY;

            const onMouseMove = (moveEvent) => {
                isDragging = true; 
                const dx = moveEvent.clientX - initialMouseX;
                const dy = moveEvent.clientY - initialMouseY;

                cardElement.style.position = 'absolute';
                cardElement.style.left = `${startX + dx}px`;
                cardElement.style.top = `${startY + dy}px`;
                event.preventDefault(); 
                saveCardPositions(boardId);
            };

            const onMouseUp = () => {
                document.removeEventListener('mousemove', onMouseMove);
                document.removeEventListener('mouseup', onMouseUp);
                isDragging = false; 
            };

            document.addEventListener('mousemove', onMouseMove);
            document.addEventListener('mouseup', onMouseUp);
        });

        cardContainer.appendChild(cardElement);
    });

    loadCardPositions(boardId);
}

//CHATGPT HELPED ME WITH saveCardPositions() and loadCardPositions()

function saveCardPositions(boardId) {
    const cardPositions = JSON.parse(localStorage.getItem('cardPositions')) || {};
    
    if (!cardPositions[boardId]) {
        cardPositions[boardId] = {};
    }

    const cards = document.querySelectorAll('#cards-container > div');
    cards.forEach(card => {
        const cardId = card.id;
        const { left, top } = card.getBoundingClientRect();
        
        cardPositions[boardId][cardId] = { x: left, y: top };
    });

    localStorage.setItem('cardPositions', JSON.stringify(cardPositions));
}

function loadCardPositions(boardId) {
    const savedPositions = JSON.parse(localStorage.getItem('cardPositions')) || {};
    
    if (savedPositions[boardId]) {
        for (const cardId in savedPositions[boardId]) {
            const card = document.getElementById(cardId);
            if (card) {
                const { x, y } = savedPositions[boardId][cardId];
                card.style.position = 'absolute';
                card.style.left = `${x}px`;
                card.style.top = `${y}px`;
            }
        }
    }
}

// WebSocket Part DO NOT DELETE!
const WS_TOKEN = localStorage.getItem('token') //|| 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2NmZlM2I2M2U0OTUwZDQ3ZWJjZjM2Y2MiLCJlbWFpbCI6Imhlcm1lc0BlbWFpbC5jb20iLCJuYW1lIjoiSGVybWVzIiwicm9sZSI6InVzZXIiLCJpYXQiOjE3MjgwNTcyMzIsImV4cCI6MTczMDY0OTIzMn0.MYl3IXAPpCC--AhOzSshcLKyY62G7Lb3Wh5BnaODhjw' // TODO: JWT TOKEN HIT
const WS_URL = localStorage.getItem('server_url') || "ws://localhost:8081" // Samma som API_URL i princip, länken till websocket servern

let socket;

function connectWebSocket(boardId, name, cards) {
    
    // Checks if the user already has a connection to a board and if it has then remove it
    if (socket) {
        console.log('Closing existing WebSocket connection');
        socket.close();  // Close the existing connection before opening a new one
    }

    //console.log('Selected Board ID:', boardId);
    //console.log(`${WS_URL}?token=${WS_TOKEN}&board_id=${boardId}`)

    // Main socket instance
    socket = new WebSocket(`${WS_URL}?token=${WS_TOKEN}&board_id=${boardId}`)

    socket.onopen = function (event) {
        console.log(`Connected to WebSocket server on board id: ${boardId} Name: ${name}`);
        document.querySelector("#websocket-test").innerText = `Connected to WebSocket Server on board ${name}!`;
    };

    /*
    console.log(`Cards within the WS func:`)
    console.log(cards)
    console.log("-------------")
    */
    // Add all the card id's to an array and add eventlisteners for a simple
    let cardIds = [];
    cards.forEach((card) => {
        cardIds.push(card.id);
        document.getElementById(card.id).addEventListener('click', () => {
            console.log(`Card with ID ${card.id} and title ${card.title} was clicked!`)
        })
    })

    console.log(`Cards ids!: `)
    console.log(cardIds);
    console.log("-------------")


    // Will be added when card functionality is done
    socket.onmessage = function (event) {
        console.log('Received message:', event.data);
        const data = JSON.parse(event.data);

        console.log(data)

        if (data.status == 0) {
            document.querySelector('#websocket-test').innerHTML = "Connected"
            document.querySelector('#card').innerHTML = data.msg;
        } else {
            document.querySelector('#websocket-test').innerHTML = "Not Connected"
        } 
    };

    socket.onclose = function (event) {
        console.log('Connection Closed');
        document.querySelector("#websocket-test").innerText = "Disconnected from WebSocket Server!";
    }
    // Send to other clients
    /*cards.addEventListener('input', (evt) => {
        socket.send(evt.target.value);    
        })
    */
}

function reconnectWebSocket() {
    console.log("Trying to connect...")
    connectWebSocket()
}

document.getElementById('new-note').addEventListener('click', createBoard);
document.getElementById('create-card-btn').addEventListener('click', createCard);

function openCardModal() {
    const cardModal = document.getElementById('card-modal');
    cardModal.classList.remove('hidden');
}

function hideCardModal() {
    const cardModal = document.getElementById('card-modal');
    cardModal.classList.add('hidden');
}

document.getElementById('open-card-modal').addEventListener('click', openCardModal);
document.getElementById('create-card-btn').addEventListener('click', hideCardModal);
document.getElementById('cancel-card-btn').addEventListener('click', hideCardModal);


// Change websocket to connect to chosen board
document.getElementById('boards-selector').addEventListener('change', (event) => {
    const selectedBoardId = event.target.value; 
    const boardName = event.target.options[event.target.selectedIndex].text;
    //console.log(`Current board: ${selectedBoardId} Name: ${boardName}`);
    const selectedBoard = boards.find(board => String(board.id) === String(selectedBoardId));
    displayBoardCards(selectedBoard.cards);
    connectWebSocket(selectedBoardId, boardName, selectedBoard.cards);

});


fetchBoards();