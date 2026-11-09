import { useState } from 'react'
import { createChore } from '../../../api.js'

function CreateChoreForm({ user, onSuccess }) {
  const [error, setError] = useState('')

  async function handleSubmit(event) {
    event.preventDefault() // stop the browser from reloading the page

    const formElement = event.target
    const form = new FormData(formElement)
    const name = form.get('name')
    const intervalDays = Number(form.get('intervalDays'))
    const effort = Number(form.get('effort'))

    try {
      const chore = await createChore(user.houseId, name, intervalDays, effort)
      setError('')
      formElement.reset()
      onSuccess(chore)
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Create a chore</h2>

      <p>
        <label>
          Chore name <input name="name" maxLength={50} required />
        </label>
      </p>

      <p>
        <label>
          Repeat every (days) <input name="intervalDays" type="number" min={1} max={7} required />
        </label>
      </p>

      <p>
        <label>
          Effort (1-3) <input name="effort" type="number" min={1} max={3} required />
        </label>
      </p>

      {error && <p>{error}</p>}

      <button type="submit">Create chore</button>
    </form>
  )
}

export default CreateChoreForm
