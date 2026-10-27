import { useState } from 'react'
import CreateHouseForm from './components/CreateHouseForm.jsx'
import JoinHouseForm from './components/JoinHouseForm.jsx'
import ViewHousematesList from './components/ViewHousematesList.jsx'

function App() {
  const [user, setUser] = useState(null)
    if (user === null) {
    return (
      <main>
        <h1>Housemate Solution</h1>
        <CreateHouseForm onSuccess={setUser} />
        <JoinHouseForm onSuccess={setUser} />
      </main>
    )
  }

  return (
    <main>
      <h1>Welcome, {user.name}!</h1>
      <p>
        You live in <strong>{user.houseName}</strong>.
      </p>
      <p>Share this join code with your housemates:</p>
      <p>{user.joinCode}</p>
      <h2>List of Housemates</h2>
      <ViewHousematesList user={user} />
      <button onClick={() => setUser(null)}>Back</button>
    </main>
  )
}

export default App
