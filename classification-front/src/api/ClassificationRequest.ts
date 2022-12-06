export async function classificateImage(base64Image: string): Promise<number> {
  return await postImageToClassificate(base64Image);
}

export async function convertToBase64(file: File): Promise<string | undefined> {
  if (!file || !file.type.includes('image')) {
    return undefined;
  }
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => {
      const value = reader.result?.toString();
      if (!value) return;

      const firstComma = value?.indexOf(',');
      if (firstComma == undefined) reject(new Error('Base64 conversion error'));

      const base64_image = value?.slice(firstComma + 1, value.length);
      if (base64_image == undefined)
        reject(new Error('Base64 conversion error'));

      resolve(base64_image);
    };

    reader.readAsDataURL(file);
    reader.onerror = reject;
  });
}

async function postImageToClassificate(base64Image: string): Promise<number> {
  const response = await fetch(
    'https://flask-production-aafb.up.railway.app/classify',
    {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ image_src: base64Image })
    }
  );
  const classifyJson = await response.json();
  return classifyJson.result as number;
}
