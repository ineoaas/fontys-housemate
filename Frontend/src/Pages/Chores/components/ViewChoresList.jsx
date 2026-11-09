import { useState, useEffect } from 'react'
import { getChores } from '../../../api.js'

function ViewChoresList({ user }) {
  const [chores, setChores] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    async function fetchChores() {
      try {
        const choresList = await getChores(user.houseId)
        setChores(choresList)
      } catch (err) {
        setError(err.message)
      }
    }

    void fetchChores()
  }, [user.houseId])

  return (
    <div>
      {error && <p>{error}</p>}
      <ul>
        {chores.map((chore) => (
          <li key={chore.id}>
            {chore.name} (every {chore.intervalDays} days)
          </li>
        ))}
      </ul>
    </div>
  )
}

export default ViewChoresList
