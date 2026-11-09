import { useState } from 'react'
import { joinHouse } from '../api.js'

function JoinHouseForm({ onSuccess }) {
  const [error, setError] = useState('')

  async function handleSubmit(event) {
    event.preventDefault() // stop the browser from reloading the page

    const form = new FormData(event.target)
    const joinCode = form.get('joinCode')
    const name = form.get('name')
    const email = form.get('email')

    try {
      const user = await joinHouse(joinCode, name, email)
      onSuccess(user)
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Join a house</h2>

      <p>
        <label>
          Join code <input name="joinCode" placeholder="e.g. K7QX2M" maxLength={20} required />
        </label>
      </p>

      <p>
        <label>
          Your name <input name="name" maxLength={50} required />
        </label>
      </p>

      <p>
        <label>
          Email <input name="email" type="email" maxLength={100} required />
        </label>
      </p>

      {error && <p>{error}</p>}

      <button type="submit">Join house</button>
    </form>
  )
}

export default JoinHouseForm
