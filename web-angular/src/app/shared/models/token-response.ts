export interface TokenResponse {
  token: string;
  issuedAt: number;
  expiresAt: number;
  durationMillisecs: number;
}
