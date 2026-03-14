async function apiFetch(url, options = {}) {
  return fetch(url, { credentials: "same-origin", ...options });
}

async function getProfileOrNull() {
  const res = await apiFetch("/api/auth/profile");
  if (res.status === 200) return res.json();
  return null;
}

async function loginRequest(login, password) {
  return apiFetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ login, password })
  });
}

async function registerRequest(login, password) {
  return apiFetch("/api/auth/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ login, password })
  });
}

async function logoutRequest() {
  return apiFetch("/logout", { method: "POST" });
}

async function createReservation(payload) {
  return apiFetch("/api/reservation", {
    method: "POST",
    headers: { "Content-Type":"application/json" },
    body: JSON.stringify(payload)
  });
}

async function loadMyReservations() {
  return apiFetch("/api/reservation", {
    method: "GET"
  });
}

async function cancelReservation(reservationId) {
  return apiFetch("/api/reservation/${reservationId}/cancel", {
    method: "POST"
  });
}