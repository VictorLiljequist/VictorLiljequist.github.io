const SERVER_URL = "http://localhost:8080";

function newUserData() {
    const userName = document.querySelector('#username').value;
    const email = document.querySelector('#email').value;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    const password = document.querySelector('#password').value;

    if (!userName) {
        document.querySelector('#signup-out').innerHTML = "You have not filled in the username form! Fill it in please!";
        document.querySelector('#signup-out').style.color = "red";
        document.querySelector('#signup-out').style.fontWeight = 'bold';
        return;
    } else if (!email) {
        document.querySelector('#signup-out').innerHTML = "You have not filled in the email form! Fill it in please!";
        document.querySelector('#signup-out').style.color = "red";
        document.querySelector('#signup-out').style.fontWeight = 'bold';
        return;
    } else if (!password) {
        document.querySelector('#signup-out').innerHTML = "You have not filled in the password form! Fill it in please!";
        document.querySelector('#signup-out').style.color = "red";
        document.querySelector('#signup-out').style.fontWeight = 'bold';
        return;
    }

    if (!emailRegex.test(email)) {
        document.querySelector('#signup-out').innerHTML = "You have not inserted a proper email!";
        document.querySelector('#signup-out').style.color = "red";
        document.querySelector('#signup-out').style.fontWeight = 'bold';
        return;
    }

    signUp(userName, email, password);
}

async function signUp(userName, email, password) {
    
    const response = await fetch(`${SERVER_URL}/users/`, {
        method: 'POST',
        body: JSON.stringify({
            name: userName,
            email: email,
            password: password
        }),
        headers: {
            "Content-Type": "application/json"
        }
    });

    const json = await response.json()

    if (json.status == 1) return document.querySelector('#signup-out').innerHTML = "Sign up failed";

    document.querySelector('#signup-out').innerHTML = "Sign up successful! <a href='http://localhost:5501/index.html'>Login Here!</a>";
}

document.querySelector('#signup-btn').addEventListener('click', newUserData);