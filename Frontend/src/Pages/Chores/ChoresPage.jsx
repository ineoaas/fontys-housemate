import { useState } from 'react'
import CreateChoreForm from './components/CreateChoreForm.jsx'
import ViewChoresList from './components/ViewChoresList.jsx'

function ChoresPage({ user }) {
  // Changing the key remounts the list, so it fetches again after a chore is created
  const [listKey, setListKey] = useState(0)

  return (
    <main>
      <h1>Chores</h1>
      <CreateChoreForm user={user} onSuccess={() => setListKey((key) => key + 1)} />
      <h2>List of Chores</h2>
      <ViewChoresList key={listKey} user={user} />
    </main>
  )
}

export default ChoresPage
