import { test, expect } from '@playwright/test'

test('creating a house opens the dashboard', async ({ page }) => {
  const name = 'name 2'
  const email = `test+${Date.now()}@example.com` // unique each run, because emails must be unique

  await page.goto('/')

  // The join form also has "Your name" and "Email" fields, so only look inside the create form
  const createForm = page.locator('form').filter({ has: page.getByRole('heading', { name: 'Create a house' }) })
  await createForm.getByLabel('House name').fill('test 2')
  await createForm.getByLabel('Your name').fill(name)
  await createForm.getByLabel('Email').fill(email)
  await createForm.getByRole('button', { name: 'Create house' }).click()

  await expect(page.getByRole('heading', { name: `Welcome, ${name}!` })).toBeVisible()
})
