const API_URL = "http://localhost:8080/api/agency";

const emptyAgency = {
    agencyId: 0,
    shortName: '',
    longName: '',
};

const MESSAGE_STYLES = {
    'SUCCESS': 'alert alert-success',
    'ERROR': 'alert alert-danger',
}


let currentView = 'list';
let agencies = [];
let currentAgency = {};

const init = () => {
    refreshList();
};

const handleAddAgency = () => {
    hideMessages();
    currentAgency = { ...emptyAgency };
    resetForm();
    setView('form');
};

const handleEdit = (id) => {
    hideMessages();
    const index = agencies.findIndex(a => a.agencyId === id);
    if (index !== -1) {
        currentAgency = agencies[index];
        resetForm();
        setView('form');
    }
};

const handleConfirmDelete = (id) => {
    hideMessages();
    const index = agencies.findIndex(a => a.agencyId === id);
    if (index !== -1) {
        currentAgency = agencies[index];
        document.getElementById('deleteTitle').innerText = currentAgency.longName;
        setView('delete');
    }
};

const handleChange = (evt) => {
    currentAgency[evt.target.name] = evt.target.value;
};

const handleSaveAgency = (evt) => {
    evt.preventDefault();

    if (currentAgency.agencyId === 0) {
        addAgency(currentAgency)
            .then(data => {
                displayMessage(`${data.longName} was added!`, 'SUCCESS');
                refreshList();
                setView('list');
            })
            .catch(errors => {
                displayMessage(errors, 'ERROR');
            });
    }
    else {
        updateAgency(currentAgency)
            .then(() => {
                displayMessage(`${currentAgency.longName} was updated!`, 'SUCCESS');
                refreshList();
                setView('list');
            })
            .catch(errors => {
                displayMessage(errors, 'ERROR');
            });
    }
};

const handleDelete = () => {
    deleteAgency(currentAgency)
        .then(() => {
            displayMessage(`${currentAgency.longName} was deleted!`, 'SUCCESS');
            refreshList();
            setView('list');
        })
        .catch(errors => {
            displayMessage(errors, 'ERROR');
        });

};

const refreshList = () => {
    fetchAgencies()
        .then(data => {
            agencies = data;
            renderList();
        })
        .catch(err => {
            displayMessage('Could not add agency', 'ERROR');
        });
};



const renderList = () => {
    let htmlString = '<div class="row">';

    for (let agency of agencies) {
        const AVATAR_URL = "https://api.dicebear.com/6.x/shapes/svg?seed=";
        const getRandomPersona = (personaNames) => {
            const randomIndex = Math.floor(Math.random() * personaNames.length);
            return personaNames[randomIndex];
        }

        const randomPersonaName = getRandomPersona(personaNameList);
        const agencyHtml = `
        <div class="col">
            <div class="card" style="width: 17rem;">
            <img class="card-img rounded-circle img-fluid" src="${AVATAR_URL}${randomPersonaName}" alt="persona-avatar">
                    <div class="card-body">
                        <h6 class="main-text">${agency.shortName}</h5>

                        <br>
                        <div class="icons my-2">
                        <button type="button" class="btn btn-primary btn-sm rounded-pill" onclick="handleEdit(${agency.agencyId})"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button type="button" class="btn btn-danger btn-sm rounded-pill" onclick="handleConfirmDelete(${agency.agencyId})"><i class="fa-solid fa-trash-can"></i></button>
                        </div>
                    </div>
            </div>   
        </div>
        <br>
        `;
        htmlString += agencyHtml;
    }

    htmlString += '</div>';
    document.getElementById('listView').innerHTML = htmlString;

};

const setView = (nextView) => {
    currentView = nextView;
    document.getElementById('formView').style.display = 'none';
    document.getElementById('listView').style.display = 'none';
    document.getElementById('deleteView').style.display = 'none';

    if (currentView === 'form') {
        document.getElementById('formView').style.display = 'block';
    } else if (currentView === 'list') {
        document.getElementById('listView').style.display = 'block';
    } else {
        document.getElementById('deleteView').style.display = 'block';
    }
};

const resetForm = () => {
    document.getElementById('shortName').value = currentAgency.shortName;
    document.getElementById('longName').value = currentAgency.longName;
};

const displayMessage = (message, messageStyle) => {
    const messagesDisplay = document.getElementById('messages');
    messagesDisplay.className = MESSAGE_STYLES[messageStyle];
    const messages = Array.isArray(message) ? message : [message];
    let messageHtml = '<ul>';
    for (let msg of messages) {
        messageHtml += `<li>${msg}</li>`;
    }
    messageHtml += '</ul>';
    messagesDisplay.innerHTML = messageHtml;
    messagesDisplay.style.display = 'block';
};

const hideMessages = () => {
    const messages = document.getElementById('messages');
    messages.style.display = 'none';
};

const fetchAgencies = async () => {
    const response = await fetch(API_URL);

    if (response.status === 200) {
        return await response.json();
    }
}

const addAgency = async (agency) => {
    const init = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(agency)
    };

    const response = await fetch(API_URL, init);

    if (response.status === 201) {
        return await response.json();
    } else {
        const errors = await response.json();
        return Promise.reject(errors);
    }
};

const updateAgency = async (agency) => {
    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(agency)
    };

    const url = `${API_URL}/${agency.agencyId}`;

    const response = await fetch(url, init);

    if (response.status === 404) {
        Promise.reject(`${agency.longName} was not found!`);
    } else if (response.status !== 204) {
        const errors = await response.json();
        Promise.reject(errors);
    }
};

const deleteAgency = async (agency) => {
    const init = {
        method: 'DELETE',
    };

    const response = await fetch(`${API_URL}/${agency.agencyId}`, init);

    if (response.status === 404) {
        Promise.reject(`${agency.longName} was not found!`);
    }
};

init();