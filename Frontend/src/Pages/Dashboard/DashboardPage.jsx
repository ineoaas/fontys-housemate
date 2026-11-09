import ViewHousematesList from './components/ViewHousematesList.jsx'

function DashboardPage({ user, onBack }) {
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
      <button onClick={onBack}>Back</button>
    </main>
  )
}

export default DashboardPage
